import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    id("org.jetbrains.intellij") version "0.3.0"
}

val kotlinVersion: String by extra
val gsonVersion: String by extra
val apacheCommonsVersion: String by extra

dependencies {
    compile(project(":aem-intellij-lang"))
    compile(project(":aem-intellij-common"))
    compile(project(":aem-intellij-inspection"))
    compile(project(":aem-intellij-index"))
    compile(project(":aem-intellij-integration"))

    compile("com.google.code.gson:gson:$gsonVersion")
    compile("org.apache.commons:commons-lang3:$apacheCommonsVersion")

    testCompile(project(":test-framework"))
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    pluginName = "aemtools"
    version = ideaVersion
    downloadSources = true
    updateSinceUntilBuild = false
    setPlugins(
            "IntelliLang"
    )
}

tasks.withType<RunIdeTask> {
    jvmArgs.add("-Didea.ProcessCanceledException=disabled")
}
