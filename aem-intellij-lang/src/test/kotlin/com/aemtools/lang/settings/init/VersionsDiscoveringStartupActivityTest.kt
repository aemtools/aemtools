package com.aemtools.lang.settings.init

import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.AemVersion
import com.intellij.notification.NotificationType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

class VersionsDiscoveringStartupActivityTest : BasePlatformTestCase() {
  fun fixture(): CodeInsightTestFixture = myFixture

  fun testFindAemVersionByUberJar6_5_15() {
    testDependency({
      artifactId = "uber-jar"
      version = "6.5.15"
    }, AemVersion.V_6_5)
  }

  fun testFindAemVersionByUberJar6_5() {
    testDependency({
      artifactId = "uber-jar"
      version = "6.5"
    }, AemVersion.V_6_5)
  }

  fun testFindAemVersionByUberJarWithEmptyVersion() {
    testDependency({
      artifactId = "uber-jar"
      version = ""
    }, AemVersion.V_6_5)
  }

  fun testFindAemVersionByUberJarWithoutVersion() {
    testDependency({
      artifactId = "uber-jar"
    }, AemVersion.V_6_5)
  }

  fun testNotFindAemVersionAbsentUberJar() {
    testDependency({
      artifactId = "acs-commons"
      version = "1.0.0"
    })
  }

  fun testFindAemVersionByUberJar6_4_8() {
    testDependency({
      artifactId = "uber-jar"
      version = "6.4.8"
    }, AemVersion.V_6_4)
  }

  fun testFindCloudAemVersion() {
    testDependency({
      artifactId = "aem-sdk-api"
      version = "2023.2.11289.20230224T170559Z-230100"
    }, AemVersion.CLOUD)
  }

  fun testFindCloudAemVersionEvenIfThereIsAemCloudSdkWithEmptyVersion() {
    testDependency({
      artifactId = "aem-sdk-api"
      version = ""
    }, AemVersion.CLOUD)
  }

  fun testFindCloudAemVersionEvenIfThereIsAemCloudSdkWithoutVersion() {
    testDependency({
      artifactId = "aem-sdk-api"
    }, AemVersion.CLOUD)
  }

  fun testNotificationCreation() {
    val settings = AemProjectSettings()
    val expectedContent = """
        Discovered versions:
        <strong>AEM version</strong>: 6.5
        <strong>HTL version</strong>: 1.4<br>
      """.trimIndent()
    val startupActivity = VersionsDiscoveringStartupActivity()

    val notification = startupActivity.createNotification(settings, fixture().project)

    assertEquals("Project Settings", notification.groupId)
    assertEquals("AEM Tools plugin configuration", notification.title)
    assertEquals(expectedContent, notification.content)
    assertEquals(NotificationType.INFORMATION, notification.type)
    assertEquals(1, notification.actions.size)
  }

  private fun testDependency(dependencyParamsUnit: DependencyParams.() -> Unit,
                             expectedAemVersion: AemVersion? = null) {
    val dependencyParams = DependencyParams().apply(dependencyParamsUnit)
    fixture().addFileToProject("pom.xml", dependencyParams.wrapInPomDependency())

    val startupActivity = VersionsDiscoveringStartupActivity()
    val aemVersion = startupActivity.findAemVersion(fixture().project)

    if (expectedAemVersion != null) {
      assertEquals(expectedAemVersion, aemVersion)
    } else {
      assertNull(aemVersion)
    }
  }

  class DependencyParams {
    var artifactId: String? = null
    var version: String? = null

    fun wrapInPomDependency(): String {
      val versionTagString = if (version != null) {
        "<version>${version}</version>"
      } else {
        ""
      }
      return """
        <?xml version="1.0" encoding="UTF-8"?>
      <project>
          <dependencies>
              <dependency>
                <groupId>com.adobe.aem</groupId>
                <artifactId>${artifactId}</artifactId>
                ${versionTagString}
            </dependency>
          </dependencies>
      </project>
      """.trimIndent()
    }
  }

}
