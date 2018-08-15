package com.aemtools.diagnostics.error.handler

import AccessTokenProvider
import com.intellij.ide.DataManager
import com.intellij.ide.plugins.IdeaPluginDescriptorImpl
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.util.Consumer
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import java.awt.Component
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author DeusBit
 */
class ErrorHandler : ErrorReportSubmitter() {
  override fun getReportActionText(): String {
    return "Report on GitHub"
  }

  override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component,
                      consumer: Consumer<SubmittedReportInfo>): Boolean {
    val project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(parentComponent))
    object : Task.Backgroundable(project, "Submit issue") {
      override fun run(indicator: ProgressIndicator) {
        indicator.text = "Issue reporting..."

        val request = HttpPost("https://api.github.com/repos/aemtools/aemtools-issues/issues")

        try {
          request.addHeader(BasicHeader("Authorization",
              "token ${AccessTokenProvider.token}"))
        } catch (ex: TokenInitializationException) {

          return
        }

        val issueOverview = createIssueOverview(events[0], additionalInfo)

        request.entity = StringEntity("{\"title\":\"User issue for version ${getPluginVersion(pluginDescriptor)}\"," +
            "\"body\":\"$issueOverview\"," +
            "\"labels\":[\"bug\"]}")
        request.addHeader(BasicHeader("Accept", "application/vnd.github.symmetra-preview+json"))
        request.addHeader(BasicHeader("Content-Type", "application/json"))

        HttpClientBuilder.create()
            .build().use {

              val execute = it.execute(request)
              execute.entity
              execute.allHeaders
            }
      }

    }.notifyFinished()

    return true
  }

  private fun getPluginVersion(pluginDescriptor: PluginDescriptor?) = if (pluginDescriptor != null
      && pluginDescriptor is IdeaPluginDescriptorImpl) {
    pluginDescriptor.version
  } else {
    StringUtils.EMPTY
  }


  fun createIssueOverview(event: IdeaLoggingEvent,
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
          EnvironmentInfoProvider.envInfo)
          .replace("\n", "\\n")
          .replace("\r", "\\r")
          .replace("\t", "\\t")
    }
  }
}

