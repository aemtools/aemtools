package com.aemtools.diagnostics.error.handler

import com.intellij.openapi.application.ApplicationInfo

/**
 * @author DeusBit
 */
object EnvironmentInfoProvider {

  val envInfo by lazy {
    """\n* Idea version: ${getIdeVersion()}\n* Java vendor: ${getJavaVendor()}
        |\n* Java version: ${getRuntimeVersion()}
        |\n* Runtime name: ${getRuntimeName()}
        |\n* OS name: ${getOsName()}
        |\n* OS version: ${getOsVersion()}
        |\n* OS architecture: ${getOsArchitecture()}""".trimMargin()
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
