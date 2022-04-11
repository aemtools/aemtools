
plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
}


dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  testCompileOnly(project(":aem-intellij-core"))
  testImplementation(project(":test-framework"))
}
