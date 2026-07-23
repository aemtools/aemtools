import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

fun properties(key: String) = providers.gradleProperty(key).get()

plugins {
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.intellij.platform.module")
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
    //create(platformType, platformVersion)
    //bundledPlugins(platformBundledPlugins.split(',').map(String::trim).filter(String::isNotEmpty))

    testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.JUnit5, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
  }

  implementation(kotlin("test"))

  implementation(project(":aem-intellij-core"))
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  implementation(libs.assertj)
  implementation(libs.mockito.core)
  implementation(libs.mockito.kotlin)

  // Use junit-bom to align versions
  // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
  implementation(platform(libs.junit.bom)) {
    because("Platform, Jupiter, and Vintage versions should match")
  }

  // JUnit Jupiter
  implementation(libs.junit.jupiter)

  // JUnit Vintage
  implementation(libs.junit)
  testRuntimeOnly(libs.junit.vintage.engine) {
    because("allows JUnit 3 and JUnit 4 tests to run")
  }

  // JUnit Suites
  implementation(libs.junit.platform.suite)

  // JUnit Platform Launcher + Console
  testRuntimeOnly(libs.junit.platform.launcher) {
    because("allows tests to run from IDEs that bundle older version of launcher")
  }
  testRuntimeOnly(libs.junit.platform.console) {
    because("needed to launch the JUnit Platform Console program")
  }

  implementation(libs.spek.api) {
    exclude(group = "org.jetbrains.kotlin")
  }
  runtimeOnly(libs.spek.junit.platform.engine) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
  implementation(libs.spek.subject.extension) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
}
