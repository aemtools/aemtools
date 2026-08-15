plugins {
  java
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.intellij.platform.module")
  id("org.jetbrains.kotlinx.kover")
}

dependencies {
  testImplementation(project(":test-framework"))
  implementation(libs.commons.text)
}
