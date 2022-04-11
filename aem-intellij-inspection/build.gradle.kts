
plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
}

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  testImplementation(project(":test-framework"))
}
