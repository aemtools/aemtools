import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.extra
import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

plugins {
  id("org.jetbrains.intellij") version "0.3.4"
}

dependencies {
  compile(project(":aem-intellij-common"))
  compile(project(":aem-intellij-lang"))

  testCompile(project(":test-framework"))
}

val ideaVersion: String by properties

intellij {
  version = ideaVersion
  setPlugins(
      "IntelliLang"
  )
}

