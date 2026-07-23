package com.aemtools.diagnostics.error.handler

import com.aemtools.diagnostics.error.handler.model.GitHubIssue
import com.aemtools.diagnostics.error.handler.provider.IssueInfoFactory
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.awt.Component

/**
 * @author DeusBit
 */
@RunWith(MockitoJUnitRunner::class)
class GitHubErrorHandlerTest {

  private lateinit var events: Array<out IdeaLoggingEvent>

  @Mock
  private lateinit var project: Project

  @Mock
  private lateinit var component: Component

  @Mock
  private lateinit var loggingEvent: IdeaLoggingEvent

  @Mock
  private lateinit var pluginDescriptor: PluginDescriptor

  @Mock
  private lateinit var issueInfoFactory: IssueInfoFactory

  @Spy
  @InjectMocks
  private lateinit var target: GitHubErrorHandler

  @Before
  fun init() {
    events = arrayOf(loggingEvent)

    doReturn(project).`when`(target).currentProject(component)
    doReturn(pluginDescriptor).`when`(target).pluginDescriptor
    doReturn(issueInfoFactory).`when`(target).issueInfoHolder()
    doNothing().`when`(target).notifyUser(org.mockito.kotlin.any(), org.mockito.kotlin.any())
  }

  @Test
  fun testShouldCreatePrefilledIssueUrl() {
    val url = target.createIssueUrl("Broken dialog", "Line one\nLine two")

    assertEquals(
        "https://github.com/aem-tools-issue-tracker/aem-tools-issues/issues/new" +
            "?title=Broken+dialog&body=Line+one%0ALine+two&labels=bug",
        url
    )
  }

  @Test
  fun testShouldPrepareGitHubIssueReportForUserReview() {
    val issue = GitHubIssue("User issue", "Stacktrace body")
    var submittedReportInfo: SubmittedReportInfo? = null

    doReturn(issue).`when`(issueInfoFactory).create(loggingEvent, pluginDescriptor, null)

    target.submit(events, null, component, Consumer { submittedReportInfo = it })

    verify(target).notifyUser(
        GitHubErrorHandler.NotificationData(
            "Report prepared",
            "Open GitHub to review and submit the issue.",
            "https://github.com/aem-tools-issue-tracker/aem-tools-issues/issues/new" +
                "?title=User+issue&body=Stacktrace+body&labels=bug",
            NotificationType.INFORMATION
        ),
        project
    )
    assertEquals(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE, submittedReportInfo?.status)
  }
}
