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

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    maven {
      setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
    }
  }
}

