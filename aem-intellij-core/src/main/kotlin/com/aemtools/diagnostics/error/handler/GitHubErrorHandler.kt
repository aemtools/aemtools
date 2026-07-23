package com.aemtools.diagnostics.error.handler

import com.aemtools.diagnostics.error.handler.provider.IssueInfoFactory
import com.aemtools.diagnostics.error.handler.provider.impl.EnvironmentInfoProviderImpl
import com.aemtools.diagnostics.error.handler.provider.impl.GitHubIssueInfoFactory
import com.intellij.ide.DataManager
import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer
import java.awt.Component
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @author DeusBit
 */
class GitHubErrorHandler : ErrorReportSubmitter() {
  val config = ErrorReportingConfig()

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
    val issue = issueInfoHolder().create(events[0], pluginDescriptor, additionalInfo)
    val url = createIssueUrl(issue.title, issue.body)
    notifyUser(
        NotificationData(
            "Report prepared",
            "Open GitHub to review and submit the issue.",
            url,
            NotificationType.INFORMATION
        ),
        project
    )
    consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE))
    return true
  }

  fun currentProject(parentComponent: Component) =
      CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent))

  fun issueInfoHolder() = GitHubIssueInfoFactory(EnvironmentInfoProviderImpl()) as IssueInfoFactory

  fun createIssueUrl(title: String, body: String): String {
    val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8)
    val encodedBody = URLEncoder.encode(body, StandardCharsets.UTF_8)
    return "https://github.com/${config.userId}/${config.repoName}/issues/new" +
        "?title=$encodedTitle&body=$encodedBody&labels=bug"
  }

  fun notifyUser(notificationData: NotificationData, project: Project?) {
    val notification = Notification(pluginDescriptor.pluginId.idString,
            notificationData.title, notificationData.text, notificationData.notificationType)
    if (notificationData.url.isNotEmpty()) {
      notification.addAction(BrowseNotificationAction("Open GitHub issue", notificationData.url))
    }
    notification.notify(project)
  }

  data class NotificationData(
          val title: String,
          val text: String,
          val url: String,
          val notificationType: NotificationType
  ) {
    constructor(title: String, notificationType: NotificationType) : this(title, "", "", notificationType)
  }
}

