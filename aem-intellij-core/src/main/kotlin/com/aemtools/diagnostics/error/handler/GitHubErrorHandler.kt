package com.aemtools.diagnostics.error.handler

import com.aemtools.diagnostics.error.handler.exception.TokenInitializationException
import com.aemtools.diagnostics.error.handler.model.CreatedGitHubIssue
import com.aemtools.diagnostics.error.handler.provider.AccessTokenHolder
import com.aemtools.diagnostics.error.handler.provider.IssueInfoFactory
import com.aemtools.diagnostics.error.handler.provider.impl.EnvironmentInfoProviderImpl
import com.aemtools.diagnostics.error.handler.provider.impl.GitHubAccessTokenHolder
import com.aemtools.diagnostics.error.handler.provider.impl.GitHubIssueInfoFactory
import com.google.gson.Gson
import com.intellij.ide.DataManager
import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener.URL_OPENING_LISTENER
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.util.Consumer
import org.apache.commons.lang3.StringUtils
import org.apache.http.HttpEntity
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import java.awt.Component

/**
 * @author DeusBit
 */
class GitHubErrorHandler : ErrorReportSubmitter() {
  val config = ErrorReportingConfig()
  val gson = Gson()

  override fun getPrivacyNoticeText(): String {
    return """
      Please provide a brief description to explain how the error occurred. 
      By submitting this bug report you are agreeing for the displayed stacktrace, 
      IDE version, Java version, Java vendor, OS name/version/arch  
      to be shared with the developers on our
      <a href="https://github.com/${config.userId}/${config.repoName}">Github</a>.
    """.trimIndent()
  }

  override fun getReportActionText(): String {
    return "Report on GitHub"
  }

  override fun submit(events: Array<out IdeaLoggingEvent>,
                      additionalInfo: String?,
                      parentComponent: Component,
                      consumer: Consumer<in SubmittedReportInfo>): Boolean {
    val project = currentProject(parentComponent)
    val reportingTask = object : Task.Backgroundable(project, "Submit issue") {
      override fun run(indicator: ProgressIndicator) {
        indicator.text = "Issue reporting..."

        val request = createRequest()
        populateHeaders(request) ?: return
        request.entity = getIssueContent(events, additionalInfo)
        createHttpClient().use {
          val execute = it.execute(request)
          when (execute.statusLine.statusCode) {
            HttpStatus.SC_CREATED -> notifyUser(
                "Report successful",
                "<a href=${extractLinkToIssue(execute.entity)}>Click to open created issue</a>",
                NotificationType.INFORMATION)
            else -> notifyUser("Report error", StringUtils.EMPTY, NotificationType.WARNING)
          }
        }
      }
    }
    startReporting(reportingTask)
    return true
  }

  fun currentProject(parentComponent: Component) =
      CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent))

  fun accessTokenHolder() = GitHubAccessTokenHolder() as AccessTokenHolder

  fun issueInfoHolder() = GitHubIssueInfoFactory(EnvironmentInfoProviderImpl()) as IssueInfoFactory

  fun getIssueContent(events: Array<out IdeaLoggingEvent>, additionalInfo: String?) =
      StringEntity(gson.toJson(issueInfoHolder().create(events[0], pluginDescriptor, additionalInfo)))

  fun startReporting(reportingTask: Task) {
    ProgressManager.getInstance().run(reportingTask)
  }

  fun createHttpClient() = HttpClientBuilder.create().build()!!

  fun createRequest(): HttpPost {
    return HttpPost("https://api.github.com/repos/${config.userId}/${config.repoName}/issues")
  }

  fun notifyUser(title: String, text: String, notificationType: NotificationType) {
    val notification = Notification(pluginDescriptor.pluginId.idString,
        title,
        text,
        notificationType,
        URL_OPENING_LISTENER)
    Notifications.Bus.notify(notification)
  }

  fun populateHeaders(request: HttpPost): HttpPost? {
    try {
      request.addHeader(BasicHeader("Authorization",
          "token ${accessTokenHolder().getToken()}"))
      request.addHeader(BasicHeader("Accept", "application/vnd.github.v3+json"))
      request.addHeader(BasicHeader("Content-Type", "application/json"))
    } catch (ex: TokenInitializationException) {
      notifyUser("Report error", StringUtils.EMPTY, NotificationType.WARNING)
      return null
    }
    return request
  }

  fun extractLinkToIssue(entity: HttpEntity?): String {
    return entity?.content?.bufferedReader()?.use {
      val readText = it.readText()
      return@use gson.fromJson(readText, CreatedGitHubIssue::class.java).htmlUrl
    } ?: ""
  }
}

