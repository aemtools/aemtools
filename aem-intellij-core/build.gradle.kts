import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra

    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.junit.platform.gradle.plugin")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.2.17"
}

val kotlin_version: String by extra
val gson_version: String by extra
val apache_commons_version: String by extra

dependencies {
    compile(project(":lang"))
    compile(project(":common"))
    compile(project(":inspection"))

    compile("com.google.code.gson:gson:$gson_version")
    compile("org.apache.commons:commons-lang3:$apache_commons_version")

    testCompile(project(":test-framework"))
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    pluginName = "aemtools"
    version = ideaVersion
    updateSinceUntilBuild = false
    setPlugins(
            "IntelliLang"
    )
}
