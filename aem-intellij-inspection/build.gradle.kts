@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  java
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatformModule)
  alias(libs.plugins.kover)
}

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  testImplementation(project(":test-framework"))

  implementation(libs.kotlinStdLib)
  implementation(libs.kotlinReflect)
  implementation(libs.kotlinStdLibJdk8)

  testImplementation(libs.assertjCore)
  testImplementation(libs.mockitoCore)
  testImplementation(libs.mockitoKotlin)

  // Use junit-bom to align versions
  // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
  implementation(platform(libs.junitBom))  {
    because("Platform, Jupiter, and Vintage versions should match")
  }

  // JUnit Jupiter
  testImplementation(libs.junitJupiter)

  // JUnit Vintage
  testImplementation(libs.junit4)
  testRuntimeOnly(libs.junitVintageEngine) {
    because("allows JUnit 3 and JUnit 4 tests to run")
  }

  // JUnit Suites
  testImplementation(libs.junitPlatformSuite)

  // JUnit Platform Launcher + Console
  testRuntimeOnly(libs.junitPlatformLauncher) {
    because("allows tests to run from IDEs that bundle older version of launcher")
  }
  testRuntimeOnly(libs.junitPlatformConsole) {
    because("needed to launch the JUnit Platform Console program")
  }

  testImplementation(libs.spekApi) {
    exclude(group = "org.jetbrains.kotlin")
  }
  testRuntimeOnly(libs.spekJunitPlatformEngine) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
  testImplementation(libs.spekSubjectExtension) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }

  // The core Kotest framework
  testImplementation(libs.kotestRunner) {
    exclude(group = "org.jetbrains.kotlinx")
  }
  testImplementation(libs.kotestFrameworkEngine) {
    exclude(group = "org.jetbrains.kotlinx")
  }
  // Assertions library (optional but highly recommended)
  testImplementation(libs.kotestAssertionsCore) {
    exclude(group = "org.jetbrains.kotlinx")
  }
  // Property-based testing (optional)
  testImplementation(libs.kotestProperty) {
    exclude(group = "org.jetbrains.kotlinx")
  }
}

//powerAssert {
//  functions = listOf("io.kotest.matchers.shouldBe")
//}
