package com.aemtools.diagnostics.error.handler

import com.intellij.ide.plugins.IdeaPluginDescriptorImpl
import com.intellij.idea.IdeaLogger
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.PluginDescriptor
import org.apache.commons.lang3.StringUtils
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author DeusBit
 */
class GitHubIssueInfoHolder: IssueInfoHolder {

  override fun getIssueDetails(event: IdeaLoggingEvent, pluginDescriptor: PluginDescriptor,
                               additionalInfo: String?): String {
    val issueOverview = createIssueOverview(event, additionalInfo)

    return "{\"title\":\"User issue for version ${getPluginVersion(pluginDescriptor)}\"," +
        "\"body\":\"$issueOverview\"," +
        "\"labels\":[\"bug\"]}"
  }

  private fun getPluginVersion(pluginDescriptor: PluginDescriptor?) = if (pluginDescriptor != null
      && pluginDescriptor is IdeaPluginDescriptorImpl) {
    pluginDescriptor.version
  } else {
    StringUtils.EMPTY
  }


  private fun createIssueOverview(event: IdeaLoggingEvent,
                                  additionalInfo: String?): String {
    val stringWriter = StringWriter()
    PrintWriter(stringWriter).use {
      event.throwable.printStackTrace(it)
      it.flush()
      val addInfo = if (additionalInfo == null) {
        StringUtils.EMPTY
      } else {
        "Additional info: \n$additionalInfo\n"
      }
      return (addInfo +
          "Stack trace:\n```\n$stringWriter\n```" +
          EnvironmentInfoProvider.envInfo +
          "\n* Last action id: ${IdeaLogger.ourLastActionId}")
          .replace("\n", "\\n")
          .replace("\r", "\\r")
          .replace("\t", "\\t")
    }
  }
}
