package com.aemtools.diagnostics.error.handler.provider.impl

import com.aemtools.diagnostics.error.handler.provider.EnvironmentInfoProvider
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import java.io.PrintWriter

/**
 * Tests for [GitHubIssueInfoFactory].
 *
 * @author Kostiantyn Diachenko
 */
@RunWith(MockitoJUnitRunner::class)
class GitHubIssueInfoFactoryTest {

  @Mock
  private lateinit var ideaLoggingEvent: IdeaLoggingEvent

  @Mock
  private lateinit var environmentInfoProvider: EnvironmentInfoProvider

  @Mock
  private lateinit var pluginDescriptor: IdeaPluginDescriptor

  lateinit var factory: GitHubIssueInfoFactory

  @Before
  fun setUp() {
    factory = GitHubIssueInfoFactory(environmentInfoProvider)
  }

  @Test
  fun shouldCreateGitHubIssue() {
    doReturn("mock-version").`when`(pluginDescriptor).version
    doReturn(ThrowableStub(null, null)).`when`(ideaLoggingEvent).throwable
    doReturn("envInfo").`when`(environmentInfoProvider).getEnvInfo()

    val gitHubIssue = factory.create(ideaLoggingEvent, pluginDescriptor, "additional info")

    assertEquals("User issue for version mock-version", gitHubIssue.title)
    assertEquals(1, gitHubIssue.labels.size)
    assertTrue(gitHubIssue.labels.contains("bug"))
    assertEquals("""
      Additional info:
      additional info
      
      Throwable Stacktrace
      
      envInfo
    """.trimIndent(), gitHubIssue.body)
  }

  internal class ThrowableStub(message: String?, cause: Throwable?) : Throwable(message, cause) {
    override fun printStackTrace(s: PrintWriter) {
      s.print("Throwable Stacktrace")
    }
  }
}
