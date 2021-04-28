import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.identity

plugins {
  id("org.jetbrains.intellij") version "0.7.2"
}

dependencies {
  testImplementation(project(":test-framework"))
}

val ideaVersion: String by project

intellij {
  version = ideaVersion
  setPlugins(
      "IntelliLang", "java"
  )
}

tasks.test {
  jvmArgs?.add("-Djdk.attach.allowAttachSelf=true")
}
