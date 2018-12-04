import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.3.12"
}

repositories {
    mavenCentral()
}

val junitVersion: String by extra
val jmockitVersion: String by extra
val assertjVersion: String by extra
val mockitoVersion: String by extra
val spekVersion: String by extra
val junitJupiterApiVersion: String by extra
val junitJupiterEngineVersion: String by extra

dependencies {
    compile(project(":aem-intellij-core"))
    compile(project(":common"))

    compile("junit:junit:$junitVersion")
    compile("org.jmockit:jmockit:$jmockitVersion")
    compile("org.assertj:assertj-core:$assertjVersion")
    compile("org.mockito:mockito-core:$mockitoVersion")
    compile("org.jetbrains.spek:spek-api:$spekVersion")

    compile("org.junit.jupiter:junit-jupiter-api:$junitJupiterApiVersion")
    compile("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    version = ideaVersion
    setPlugins("IntelliLang")
}
