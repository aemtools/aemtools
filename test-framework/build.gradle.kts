import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatformModule)
}

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
    localPlatformArtifacts()
  }
}

intellijPlatform {
  buildSearchableOptions = false
  instrumentCode = false
}

dependencies {
  intellijPlatform {
    testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
  }

  implementation(kotlin("test"))

  implementation(project(":aem-intellij-core"))
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  implementation(libs.assertjCore)
  implementation(libs.mockitoCore)
  implementation(libs.mockitoKotlin)

  // Use junit-bom to align versions
  // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
  implementation(platform(libs.junitBom))  {
    because("Platform, Jupiter, and Vintage versions should match")
  }

  // JUnit Jupiter
  implementation(libs.junitJupiter)

  // JUnit Vintage
  implementation(libs.junit4)
  testRuntimeOnly(libs.junitVintageEngine) {
    because("allows JUnit 3 and JUnit 4 tests to run")
  }

  // JUnit Suites
  implementation(libs.junitPlatformSuite)

  // JUnit Platform Launcher + Console
  testRuntimeOnly(libs.junitPlatformLauncher)
  testRuntimeOnly(libs.junitPlatformConsole)

  implementation(libs.spekApi) {
    exclude(group = "org.jetbrains.kotlin")
  }
  runtimeOnly(libs.spekJunitPlatformEngine) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
  implementation(libs.spekSubjectExtension) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }

  // The core Kotest framework
  implementation(libs.kotestRunner) {
    exclude(group = "org.jetbrains.kotlinx")
  }
  implementation(libs.kotestFrameworkEngine) {
    exclude(group = "org.jetbrains.kotlinx")
  }
  // Assertions library (optional but highly recommended)
  implementation(libs.kotestAssertionsCore) {
    exclude(group = "org.jetbrains.kotlinx")
  }
  // Property-based testing (optional)
  implementation(libs.kotestProperty) {
    exclude(group = "org.jetbrains.kotlinx")
  }
}
