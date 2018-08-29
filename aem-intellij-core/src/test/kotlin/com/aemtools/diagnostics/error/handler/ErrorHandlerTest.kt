package com.aemtools.diagnostics.error.handler

import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.*
import org.apache.commons.lang.StringUtils
import org.apache.http.StatusLine
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicHeader
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*

import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import java.awt.Component
import kotlin.test.assertEquals


/**
 * @author DeusBit
 */
@RunWith(MockitoJUnitRunner::class)
class ErrorHandlerTest {

  private val testToken: String = "testToken"

  private lateinit var events: Array<out IdeaLoggingEvent>

  @Mock
  private lateinit var postRequest: HttpPost

  @Mock
  private lateinit var httpClient: CloseableHttpClient

  @Mock
  private lateinit var accessTokenHolder: AccessTokenHolder

  @Mock
  private lateinit var project: Project

  @Mock
  private lateinit var component: Component

  @Mock
  private lateinit var loggingEvent: IdeaLoggingEvent

  @Mock
  private lateinit var indicator: ProgressIndicator

  @Mock
  private lateinit var pluginDescriptor: PluginDescriptor

  @Mock
  private lateinit var issueInfoHolder: IssueInfoHolder

  @Mock
  private lateinit var response: CloseableHttpResponse

  @Mock
  private lateinit var statusLine: StatusLine

  @Spy
  @InjectMocks
  private lateinit var target: ErrorHandler

  @Before
  fun init() {
    doReturn(postRequest).`when`(target).createRequest()
    doReturn(httpClient).`when`(target).createHttpClient()
    doReturn(accessTokenHolder).`when`(target).accessTokenHolder()
    doReturn(project).`when`(target).currentProject(component)

    events = arrayOf(loggingEvent)

    doAnswer {
      val backgroundable = it.getArgument(0) as Task.Backgroundable
      backgroundable.run(indicator)
    }.`when`(target).startReporting(any())

    doReturn(pluginDescriptor).`when`(target).pluginDescriptor
    doReturn(issueInfoHolder).`when`(target).issueInfoHolder()
    doReturn(StringUtils.EMPTY).`when`(issueInfoHolder).getIssueDetails(loggingEvent, pluginDescriptor, null)
    doReturn(response).`when`(httpClient).execute(any())
    doReturn(statusLine).`when`(response).statusLine
    doNothing().`when`(target).notifyUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), any())
  }

  @Test
  fun testAccessTokenShouldBeAddedToRequestHeader() {
    doReturn(testToken).`when`(accessTokenHolder).getToken()

    target.submit(events, null, component, {})

    argumentCaptor<BasicHeader>().apply {
      verify(postRequest, times(3)).addHeader(capture())

      assertEquals("Authorization", firstValue.name)
      assertEquals("token $testToken", firstValue.value)
    }
  }

  @Test
  fun testAcceptHeaderShouldBeAddedToRequest() {
    doReturn(testToken).`when`(accessTokenHolder).getToken()

    target.submit(events, null, component, {})

    argumentCaptor<BasicHeader>().apply {
      verify(postRequest, times(3)).addHeader(capture())

      assertEquals("Accept", secondValue.name)
      assertEquals("application/vnd.github.symmetra-preview+json", secondValue.value)
    }
  }

  @Test
  fun testContentTypeHeaderShouldBeAddedToRequest() {
    doReturn(testToken).`when`(accessTokenHolder).getToken()

    target.submit(events, null, component, {})

    argumentCaptor<BasicHeader>().apply {
      verify(postRequest, times(3)).addHeader(capture())

      assertEquals("Content-Type", lastValue.name)
      assertEquals("application/json", lastValue.value)
    }
  }

  @Test
  fun testShouldShowSuccessNotificationWhenUserIssueHadPublishedToGitHub() {

    doReturn(201).`when`(statusLine).statusCode

    target.submit(events, null, component, {})

    verify(target).notifyUser("Report successful","<a href=>click to open</a>", NotificationType.INFORMATION)
  }

  @Test
  fun testShouldShowWarningNotificationWhenUserIssueHadNotPublishedToGitHub() {

    doReturn(502).`when`(statusLine).statusCode

    target.submit(events, null, component, {})

    verify(target).notifyUser("Report error",StringUtils.EMPTY, NotificationType.WARNING)
  }
}
