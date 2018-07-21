import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.extra
import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

buildscript {
    val kotlinVersion: String by properties

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }

}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.3.4"
}

dependencies {
    compile(project(":aem-intellij-common"))
    compile(project(":aem-intellij-lang"))

    testCompile(project(":test-framework"))
}

val ideaVersion: String by properties

configure<IntelliJPluginExtension> {
    version = ideaVersion
    setPlugins(
            "IntelliLang", "github", "git4idea"
    )
}
