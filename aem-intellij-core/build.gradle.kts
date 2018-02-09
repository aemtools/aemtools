import org.jetbrains.intellij.IntelliJPluginExtension
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
    id("org.jetbrains.intellij") version "0.2.17"
}

java.sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
}

val kotlinVersion: String by extra
val gsonVersion: String by extra
val apacheCommonsVersion: String by extra

dependencies {
    compile(project(":lang"))
    compile(project(":common"))
    compile(project(":inspection"))

    compile("com.google.code.gson:gson:$gsonVersion")
    compile("org.apache.commons:commons-lang3:$apacheCommonsVersion")

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
