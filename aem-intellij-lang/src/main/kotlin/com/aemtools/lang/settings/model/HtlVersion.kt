package com.aemtools.lang.settings.model

enum class HtlVersion(val version: String) {
  V_1_3("1.3"),
  V_1_4("1.4");

  companion object {
    fun versions() = HtlVersion.values().map { it.version }

    fun getFirstCompatibleWith(aemVersion: AemVersion) = when (aemVersion) {
      AemVersion.V_6_3,
      AemVersion.V_6_4,
      AemVersion.V_6_5 -> V_1_4
      else -> V_1_3
    }
  }
}
