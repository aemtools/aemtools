package com.aemtools.lang.settings.model

/**
 * Represents supported HTL minor versions.
 *
 * @author Kostiantyn Diachenko
 */
enum class HtlVersion(val version: String) {
  V_1_3("1.3"),
  V_1_4("1.4");

  fun isAtLeast(htlVersion: HtlVersion): Boolean = this.ordinal >= htlVersion.ordinal

  companion object {
    fun versions() = HtlVersion.values().map { it.version }

    fun fromVersion(version: String): HtlVersion? =
        HtlVersion.values().firstOrNull { version == it.version }

    fun getFirstCompatibleWith(aemVersion: AemVersion) = when (aemVersion) {
      AemVersion.V_6_3,
      AemVersion.V_6_4,
      AemVersion.V_6_5,
      AemVersion.CLOUD -> V_1_4
      else -> V_1_3
    }

    fun latest() = HtlVersion.values().last()
  }
}
