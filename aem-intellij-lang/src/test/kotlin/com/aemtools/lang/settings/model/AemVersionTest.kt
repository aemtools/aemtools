package com.aemtools.lang.settings.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AemVersionTest {
  @ParameterizedTest
  @CsvSource(value = [
    "6.1,V_6_1",
    "6.1.1,V_6_1",
    "6.2,V_6_2",
    "6.2.1,V_6_2",
    "6.3.1,V_6_3",
    "6.4,V_6_4",
    "6.4.1,V_6_4",
    "6.5,V_6_5",
    "6.5.1,V_6_5",
    "AEMaaCS,CLOUD",
    "2023.5.11983.20230511T173830Z-230200,CLOUD",
    "2023.4.11873.20230421T153841Z-230200,CLOUD",
    "2023.2.11289.20230224T170559Z-230100,CLOUD",
    "5.6,"
  ])
  fun testGetFirstCompatibleWith(version: String, expectedAemVersion: String?) {
    if (expectedAemVersion != null) {
      assertEquals(AemVersion.valueOf(expectedAemVersion), AemVersion.fromFullVersion(version))
    } else {
      assertNull(AemVersion.fromFullVersion(version))
    }
  }

  @Test
  fun testLatestHtlVersion() {
    assertEquals(AemVersion.V_6_5, AemVersion.latest())
  }

  @Test
  fun testCountOfSupportedVersions() {
    assertEquals(6, AemVersion.versions().size)
  }
}
