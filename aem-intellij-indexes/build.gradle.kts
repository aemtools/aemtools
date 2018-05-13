import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.identity

buildscript {
    val kotlinVersion: String by extra

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
    id("org.jetbrains.intellij") version "0.3.0"
}

dependencies {
    compile(project(":lang"))
    compile(project(":common"))
    testCompile(project(":test-framework"))
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    version = ideaVersion

    setPlugins(
            "IntelliLang"
    )
}
