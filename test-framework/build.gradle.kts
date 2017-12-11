import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

apply {
    plugin("java")
    plugin("kotlin")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.2.17"
}

repositories {
    mavenCentral()
}

val junit_version: String by extra
val jmockit_version: String by extra
val assertj_version: String by extra
val mockito_version: String by extra

dependencies {
    compile(project(":aem-intellij-core"))
    compile(project(":common"))

    compile("junit:junit:$junit_version")
    compile("org.jmockit:jmockit:$jmockit_version")
    compile("org.assertj:assertj-core:$assertj_version")
    compile("org.mockito:mockito-core:$mockito_version")
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    version = ideaVersion
    setPlugins("IntelliLang")
}
