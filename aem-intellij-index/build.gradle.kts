import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.identity

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
    compile(project(":aem-intellij-lang"))
    compile(project(":aem-intellij-common"))
    testCompile(project(":test-framework"))
}

val ideaVersion: String by properties

configure<IntelliJPluginExtension> {
    version = ideaVersion

    setPlugins(
            "IntelliLang", "github", "git4idea"
    )
}
