import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "0.8.1"

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
    plugin("org.jetbrains.intellij")
}

plugins {
    java
}

repositories {
    mavenCentral()
}

val kotlin_version: String by extra
val junit_version: String by extra
val jmockit_version: String by extra
val assertj_version: String by extra
val mockito_version: String by extra

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlin_version))

    implementation(rootProject)
    implementation(project(":common"))

    implementation("junit", "junit", junit_version)
    implementation("org.mockito", "mockito-core", mockito_version)
    implementation("org.jmockit", "jmockit", jmockit_version)
    implementation("org.assertj", "assertj-core", assertj_version)
}

configure<IntelliJPluginExtension> {
    version = "2017.2.5"
    setPlugins("IntelliLang")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


