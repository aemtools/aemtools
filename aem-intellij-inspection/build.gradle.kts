fun properties(key: String) = project.findProperty(key).toString()

plugins {
  id("org.jetbrains.intellij")
}

apply {
  plugin("java")
  plugin("kotlin")
  plugin("org.jetbrains.intellij")
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

  testImplementation(project(":test-framework"))
}
