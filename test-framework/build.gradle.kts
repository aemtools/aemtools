val kotlinVersion: String by extra
val mockitoKotlinVersion: String by extra
val spekVersion: String by extra
var junit4Version: String by extra
var junitBomVersion: String by extra
val assertjVersion: String by extra
val mockitoVersion: String by extra

plugins {
  kotlin("jvm")
  id("org.jetbrains.intellij")
}

dependencies {
  implementation(project(":aem-intellij-core"))
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  implementation("org.assertj:assertj-core:$assertjVersion")
  implementation("org.mockito:mockito-core:$mockitoVersion")
  implementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")

  // Use junit-bom to align versions
  // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
  implementation(platform("org.junit:junit-bom:$junitBomVersion")) {
    because("Platform, Jupiter, and Vintage versions should match")
  }

  // JUnit Jupiter
  implementation("org.junit.jupiter:junit-jupiter")

  // JUnit Vintage
  implementation("junit:junit:$junit4Version")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine") {
    because("allows JUnit 3 and JUnit 4 tests to run")
  }

  // JUnit Suites
  implementation("org.junit.platform:junit-platform-suite")

  // JUnit Platform Launcher + Console
  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
    because("allows tests to run from IDEs that bundle older version of launcher")
  }
  testRuntimeOnly("org.junit.platform:junit-platform-console") {
    because("needed to launch the JUnit Platform Console program")
  }

  implementation("org.jetbrains.spek:spek-api:$spekVersion") {
    exclude(group = "org.jetbrains.kotlin")
  }
  runtimeOnly("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion") {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
  implementation("org.jetbrains.spek:spek-subject-extension:$spekVersion") {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
}
