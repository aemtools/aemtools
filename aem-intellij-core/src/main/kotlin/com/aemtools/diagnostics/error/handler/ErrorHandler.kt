package com.aemtools.diagnostics.error.handler

import com.intellij.ide.DataManager
import com.intellij.ide.plugins.IdeaPluginDescriptorImpl
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.util.Consumer
import org.apache.commons.lang3.StringUtils
import org.apache.http.auth.AuthState
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.entity.StringEntity
import org.apache.http.impl.auth.BasicScheme
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import org.jetbrains.plugins.github.util.GithubAuthData
import org.jetbrains.plugins.github.util.GithubAuthDataHolder
import org.jetbrains.plugins.github.util.GithubUtil
import java.awt.Component
import java.io.PrintWriter
import java.io.StringWriter

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

        GithubUtil.runTask(myProject, GithubAuthDataHolder.createFromSettings(), indicator) {
          val authData = GithubAuthDataHolder.createFromSettings().authData

          //              val request = HttpPost("https://api.github.com/repos/aemtools/aemtools-issues/issues")
          val request = HttpPost("https://api.github.com/repos/DeusBit/test/issues")
          val context: HttpContext?

          when {
            authData.authType == GithubAuthData.AuthType.BASIC -> {
              context = createContextWithBasicAuthentication(authData)
            }
            authData.authType == GithubAuthData.AuthType.TOKEN -> {
              context = null
              request.addHeader(BasicHeader("Authorization",
                  "token ${authData.tokenAuth?.token ?: StringUtils.EMPTY}"))
            }
            else -> {
              context = null
            }
          }

          val issueOverview = createIssueOverview(events[0], pluginDescriptor, additionalInfo)

          request.entity = StringEntity("{\"title\":\"User issue for version ${getPluginVersion(pluginDescriptor)}\"," +
              "\"body\":\"$issueOverview\"," +
              "\"labels\":[\"bug\"]}")
          request.addHeader(BasicHeader("Accept", "application/vnd.github.symmetra-preview+json"))
          request.addHeader(BasicHeader("Content-Type", "application/json"))

          HttpClientBuilder.create()
              .build().use {

                val execute = it.execute(request, context)
                execute.entity
                execute.allHeaders
              }
        }
      }

    }.notifyFinished()

    return true
  }

  private fun getIdeVersion(): String {
    return ApplicationInfo.getInstance().strictVersion
  }

  private fun getRuntimeVersion() = System.getProperty("java.runtime.version") ?: System.getProperty("java.version")

  private fun getRuntimeName() = System.getProperty("java.runtime.name")

  private fun getJavaVendor() = System.getProperty("java.vendor")

  private fun getOsName() = System.getProperty("os.name")

  private fun getOsVersion() = System.getProperty("os.version")

  private fun getOsArchitecture() = System.getProperty("os.arch")

  private fun getPluginVersion(pluginDescriptor: PluginDescriptor?) = if (pluginDescriptor != null
      && pluginDescriptor is IdeaPluginDescriptorImpl) {
    pluginDescriptor.version
  } else {
    StringUtils.EMPTY
  }

  private fun createContextWithBasicAuthentication(authData: GithubAuthData): BasicHttpContext {
    val context = BasicHttpContext()
    val authState = AuthState()
    authState.update(BasicScheme(),
        UsernamePasswordCredentials(authData.basicAuth?.login, authData.basicAuth?.password))
    context.setAttribute(HttpClientContext.TARGET_AUTH_STATE, authState)
    return context
  }

  private fun createIssueOverview(event: IdeaLoggingEvent,
                                  pluginDescriptor: PluginDescriptor?,
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
      return StringBuilder(addInfo).append("Stack trace:\n```\n$stringWriter\n```")
          .append("\n* Plugin version: ${getPluginVersion(pluginDescriptor)}")
          .append("\n* Idea version: ${getIdeVersion()}")
          .append("\n* Java vendor: ${getJavaVendor()}")
          .append("\n* Java version: ${getRuntimeVersion()}")
          .append("\n* Runtime name: ${getRuntimeName()}")
          .append("\n* OS name: ${getOsName()}")
          .append("\n* OS version: ${getOsVersion()}")
          .append("\n* OS architecture: ${getOsArchitecture()}")
          .toString()
          .replace("\n", "\\n")
          .replace("\r", "\\r")
          .replace("\t", "\\t")
    }
  }
}

