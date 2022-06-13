package com.aemtools.diagnostics.error.handler.provider.impl

import com.aemtools.diagnostics.error.handler.provider.EnvironmentInfoProvider
import com.intellij.openapi.application.ApplicationInfo

/**
 * @author DeusBit
 */
class EnvironmentInfoProviderImpl : EnvironmentInfoProvider {

  override fun getEnvInfo(): String {
    return buildString {
      appendLine("* Idea version: ${getIdeVersion()}")
      appendLine("* Java vendor: ${getJavaVendor()}")
      appendLine("* Java version: ${getRuntimeVersion()}")
      appendLine("* Runtime name: ${getRuntimeName()}")
      appendLine("* OS name: ${getOsName()}")
      appendLine("* OS version: ${getOsVersion()}")
      appendLine("* OS architecture: ${getOsArchitecture()}")
    }
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

}
