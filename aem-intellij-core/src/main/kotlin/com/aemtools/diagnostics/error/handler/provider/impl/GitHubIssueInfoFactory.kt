package com.aemtools.diagnostics.error.handler.provider.impl

import com.aemtools.diagnostics.error.handler.model.GitHubIssue
import com.aemtools.diagnostics.error.handler.provider.EnvironmentInfoProvider
import com.aemtools.diagnostics.error.handler.provider.IssueInfoFactory
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.PluginDescriptor
import org.apache.commons.lang3.StringUtils
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author DeusBit
 */
class GitHubIssueInfoFactory(private val envInfoProvider: EnvironmentInfoProvider) : IssueInfoFactory {

  override fun create(event: IdeaLoggingEvent,
                      pluginDescriptor: PluginDescriptor,
                      additionalInfo: String?): GitHubIssue {
    return GitHubIssue(
        title = "User issue for version ${getPluginVersion(pluginDescriptor)}",
        body = createIssueOverview(event, additionalInfo),
        labels = listOf("bug")
    )
  }

  private fun getPluginVersion(pluginDescriptor: PluginDescriptor?) =
      if (pluginDescriptor != null && pluginDescriptor is IdeaPluginDescriptor) {
        pluginDescriptor.version
      } else {
        StringUtils.EMPTY
      }

  private fun createIssueOverview(event: IdeaLoggingEvent,
                                  additionalInfo: String?): String {
    val stringWriter = StringWriter()
    val stacktraceWriter = PrintWriter(stringWriter)
    event.throwable.printStackTrace(stacktraceWriter)
    stacktraceWriter.flush()

    return buildString {
      if (additionalInfo == null) {
        appendLine()
      } else {
        appendLine("Additional info: ")
        appendLine(additionalInfo)
      }
      appendLine(stringWriter.toString())
      appendLine(envInfoProvider.getEnvInfo())
    }
  }
}
