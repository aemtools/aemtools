apply {
  plugin("java")
  plugin("kotlin")
  plugin("org.jetbrains.intellij")
}

plugins {
  id("org.jetbrains.intellij")
}

buildscript {
  val kotlinVersion: String by extra

  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(kotlin("gradle-plugin", kotlinVersion))
  }
}

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  testImplementation(project(":aem-intellij-core"))
  testImplementation(project(":test-framework"))
}
