plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
}

dependencies {
  testImplementation(project(":test-framework"))
}
