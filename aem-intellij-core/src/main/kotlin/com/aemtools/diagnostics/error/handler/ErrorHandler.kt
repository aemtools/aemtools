package com.aemtools.diagnostics.error.handler

import com.google.gson.JsonParser
import com.intellij.ide.DataManager
import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
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
class ErrorHandler : ErrorReportSubmitter() {

  override fun getReportActionText(): String {
    return "Report on GitHub"
  }

  override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component,
                      consumer: Consumer<SubmittedReportInfo>): Boolean {
    val project = currentProject(parentComponent)
    val reportingTask = object : Task.Backgroundable(project, "Submit issue") {
      override fun run(indicator: ProgressIndicator) {
        indicator.text = "Issue reporting..."

        val request = createRequest()

        try {
          request.addHeader(BasicHeader("Authorization",
              "token ${accessTokenHolder().getToken()}"))
        } catch (ex: TokenInitializationException) {

          return
        }

        request.entity = StringEntity(issueInfoHolder().getIssueDetails(events[0], pluginDescriptor, additionalInfo))
        request.addHeader(BasicHeader("Accept", "application/vnd.github.symmetra-preview+json"))
        request.addHeader(BasicHeader("Content-Type", "application/json"))

        createHttpClient().use {
          val execute = it.execute(request)
          if (execute.statusLine.statusCode == HttpStatus.SC_CREATED) {
            notifyUser("Report successful", "<a href=${extractLinkToIssue(execute.entity)}>click to open</a>", NotificationType.INFORMATION)
          } else {
            notifyUser("Report error", StringUtils.EMPTY, NotificationType.WARNING)
          }
        }
      }
    }

    startReporting(reportingTask)

    return true
  }

  fun currentProject(parentComponent: Component) =
      CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent))

  fun startReporting(reportingTask: Task) {
    ProgressManager.getInstance().run(reportingTask)
  }

  fun createHttpClient() = HttpClientBuilder.create().build()!!

  fun accessTokenHolder() = GitHubAccessTokenHolder() as AccessTokenHolder

  fun issueInfoHolder() = GitHubIssueInfoHolder() as IssueInfoHolder

  fun createRequest(): HttpPost {
    return HttpPost("https://api.github.com/repos/aemtools-issue-reporter/test/issues")
  }

  fun notifyUser(title: String, text: String, notificationType: NotificationType) {
    Notifications.Bus.notify(Notification(pluginDescriptor.pluginId.idString,
        title,
        text,
        notificationType,
        NotificationListener.UrlOpeningListener(true)))

  }

  fun extractLinkToIssue(entity: HttpEntity?): String {
    return entity?.content?.bufferedReader()?.use {
      val readText = it.readText()
      return@use JsonParser().parse(readText).asJsonObject.getAsJsonPrimitive("html_url").toString()
    } ?: ""
  }
}

