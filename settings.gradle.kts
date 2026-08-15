pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "aemtools"
rootProject.buildFileName = "build.gradle.kts"

include(
    "aem-intellij-core",
    "aem-intellij-lang",
    "aem-intellij-common",
    "aem-intellij-index",
//  "aem-intellij-integration",
    "aem-intellij-inspection",
    "test-framework"
)
