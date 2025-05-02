val gsonVersion = providers.gradleProperty("gsonVersion").get()
val apacheCommonsVersion = providers.gradleProperty("apacheCommonsVersion").get()
val apacheCommonsTextVersion = providers.gradleProperty("apacheCommonsTextVersion").get()

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij.platform.module")
  id("org.jetbrains.changelog")
  id("org.jetbrains.kotlinx.kover")
}

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))
  implementation(project(":aem-intellij-inspection"))
  implementation(project(":aem-intellij-index"))

  implementation("com.google.code.gson:gson:$gsonVersion")
  implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")
  implementation("org.apache.commons:commons-text:$apacheCommonsTextVersion")

  testImplementation(project(":test-framework"))
}
