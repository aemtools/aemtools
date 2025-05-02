val apacheCommonsTextVersion = providers.gradleProperty("apacheCommonsTextVersion").get()

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij.platform.module")
  id("org.jetbrains.kotlinx.kover")
}

dependencies {
  testImplementation(project(":test-framework"))
  implementation("org.apache.commons:commons-text:$apacheCommonsTextVersion")
}
