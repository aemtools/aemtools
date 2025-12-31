plugins {
  java
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatformModule)
  alias(libs.plugins.kover)
}

dependencies {
  testImplementation(project(":test-framework"))

  implementation(libs.apacheCommonsText)

  implementation(libs.kotlinStdLib)
  implementation(libs.kotlinReflect)
  implementation(libs.kotlinStdLibJdk8)

  testImplementation(libs.assertjCore)
  testImplementation(libs.mockitoCore)
  testImplementation(libs.mockitoKotlin)

  // Use junit-bom to align versions
  // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
  implementation(platform(libs.junitBom))  {
    because("Platform, Jupiter, and Vintage versions should match")
  }

  // JUnit Jupiter
  testImplementation(libs.junitJupiter)

  // JUnit Vintage
  testImplementation(libs.junit4)
  testRuntimeOnly(libs.junitVintageEngine) {
    because("allows JUnit 3 and JUnit 4 tests to run")
  }

  // JUnit Suites
  testImplementation(libs.junitPlatformSuite)

  // JUnit Platform Launcher + Console
  testRuntimeOnly(libs.junitPlatformLauncher) {
    because("allows tests to run from IDEs that bundle older version of launcher")
  }
  testRuntimeOnly(libs.junitPlatformConsole) {
    because("needed to launch the JUnit Platform Console program")
  }

  testImplementation(libs.spekApi) {
    exclude(group = "org.jetbrains.kotlin")
  }
  testRuntimeOnly(libs.spekJunitPlatformEngine) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
  testImplementation(libs.spekSubjectExtension) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
}
