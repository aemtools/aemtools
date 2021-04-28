import org.jetbrains.intellij.IntelliJPluginExtension

buildscript {
    val kotlinVersion: String by extra

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.junit.platform.gradle.plugin")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.7.2"
}

val kotlinVersion: String by extra
val gsonVersion: String by extra
val apacheCommonsVersion: String by extra

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))
  implementation(project(":aem-intellij-inspection"))
  implementation(project(":aem-intellij-index"))

  implementation("com.google.code.gson:gson:$gsonVersion")
  implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")

  testImplementation(project(":test-framework"))
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    pluginName = "aemtools"
    version = ideaVersion
    updateSinceUntilBuild = false
    setPlugins(
            "IntelliLang", "java"
    )
}

tasks.test {

}
