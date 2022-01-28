apply {
  plugin("java")
  plugin("kotlin")
  plugin("org.jetbrains.intellij")
}

plugins {
  java
  id("org.jetbrains.intellij")
}

buildscript {
  val kotlinVersion: String by extra

  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }

  dependencies {
    classpath(kotlin("gradle-plugin", kotlinVersion))
  }
}

dependencies {
  testImplementation(project(":test-framework"))
}
