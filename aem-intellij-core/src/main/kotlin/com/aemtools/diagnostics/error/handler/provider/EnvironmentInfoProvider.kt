package com.aemtools.diagnostics.error.handler.provider

/**
 * @author Kostiantyn Diachenko
 */
interface EnvironmentInfoProvider {
  fun getEnvInfo(): String
}
