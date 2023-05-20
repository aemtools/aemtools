plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
  id("org.jetbrains.kotlinx.kover")
}

dependencies {
  testImplementation(project(":test-framework"))
}
