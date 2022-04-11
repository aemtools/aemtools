plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
}

buildscript {

  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }
}

dependencies {
  testImplementation(project(":test-framework"))
}
