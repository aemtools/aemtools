import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.extra
import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

plugins {
  id("org.jetbrains.intellij") version "0.7.2"
}

dependencies {
  compile(project(":aem-intellij-common"))
  compile(project(":aem-intellij-lang"))

  testCompile(project(":test-framework"))
}

val ideaVersion: String by project

intellij {
  version = ideaVersion
  setPlugins(
      "IntelliLang", "java"
  )
}

