package com.aemtools.lang.settings.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class HtlVersionTest {

  @ParameterizedTest
  @CsvSource(value = [
    "6.1,1.3",
    "6.2,1.3",
    "6.3,1.4",
    "6.4,1.4",
    "6.5,1.4",
    "AEMaaCS,1.4"
  ])
  fun testGetFirstCompatibleWith(aemVersion: String, expectedHtlVersion: String) {
    assertEquals(expectedHtlVersion, HtlVersion.getFirstCompatibleWith(AemVersion.fromFullVersion(aemVersion)!!).version)
  }

  @Test
  fun testLatestHtlVersion() {
    assertEquals(HtlVersion.V_1_4, HtlVersion.latest())
  }

  @Test
  fun testCountOfSupportedVersions() {
    assertEquals(2, HtlVersion.versions().size)
  }

  @Test
  fun testFormattingSupportedVersion() {
    assertEquals(HtlVersion.V_1_4, HtlVersion.fromVersion("1.4"))
  }

  @Test
  fun testFormattingUnsupportedVersion() {
    assertNull(HtlVersion.fromVersion("1.4.1"))
  }
}
