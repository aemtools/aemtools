@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij.platform.module")
  id("org.jetbrains.kotlinx.kover")
  id("io.kotest").version("6.0.7")
//  id("org.jetbrains.kotlin.plugin.power-assert")
}

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  testImplementation(project(":test-framework"))

  // The core Kotest framework
  testImplementation("io.kotest:kotest-runner-junit5:6.0.7")
  testImplementation("io.kotest:kotest-framework-engine:6.0.7")
  // Assertions library (optional but highly recommended)
  testImplementation("io.kotest:kotest-assertions-core:6.0.7")

  // Property-based testing (optional)
  testImplementation("io.kotest:kotest-property:6.0.7")
}

//powerAssert {
//  functions = listOf("io.kotest.matchers.shouldBe")
//}
