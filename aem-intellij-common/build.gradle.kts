val apacheCommonsTextVersion: String by extra

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
  id("org.jetbrains.kotlinx.kover")
}

dependencies {
  testImplementation(project(":test-framework"))
  implementation("org.apache.commons:commons-text:$apacheCommonsTextVersion")
}
