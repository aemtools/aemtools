import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
import org.gradle.api.plugins.ExtensionAware

import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension

buildscript {
    val kotlin_version: String by extra

    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }

        dependencies {
            classpath(kotlin("gradle-plugin", kotlin_version))
            classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.2")
        }
    }
}

val aemtools_version: String by extra

allprojects {
    group = "aemtools"

    version = aemtools_version

    apply {
        plugin("jacoco")
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/spek")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

val kotlin_version: String by extra
val junit_version: String by extra
val jmockit_version: String by extra
val assertj_version: String by extra
val mockito_version: String by extra

plugins {
    java
}

subprojects {
    plugins {
        java
    }
    apply {
        plugin("java")
        plugin("kotlin")
        plugin("jacoco")
        plugin("org.junit.platform.gradle.plugin")
    }

    configure<JacocoPluginExtension> {
        toolVersion = "0.7.6.201602180812"
        reportsDir = file("$buildDir/jacocoReport")
    }

    afterEvaluate {
        val junitPlatformTest: JavaExec by tasks
        configure<JacocoPluginExtension> {
            applyTo(junitPlatformTest)
        }

        task<JacocoReport>("junitPlatformJacoco") {
            sourceDirectories = files("$projectDir/src/main")
            classDirectories = files("$buildDir/classes/main")
            reports {
                xml.isEnabled = true
                xml.destination =
                        file("$buildDir/reports/jacoco/test/jacocoTestReport.xml")
                csv.isEnabled = false
                html.isEnabled = true
            }
            executionData(junitPlatformTest)
        }
    }

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
    }

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
        compile("org.jetbrains.kotlin:kotlin-runtime:$kotlin_version")
        compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

        testCompile("com.github.markusbernhardt:proxy-vole:1.0.3")

        testCompile("junit:junit:$junit_version")
        testCompile("org.jmockit:jmockit:$jmockit_version")
        testCompile("org.assertj:assertj-core:$assertj_version")
        testCompile("org.mockito:mockito-core:$mockito_version")

        testCompile("org.jetbrains.spek:spek-api:1.1.5")
        testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
        testCompile("org.junit.jupiter:junit-jupiter-api:5.0.2")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.2")
        testRuntime("org.junit.vintage:junit-vintage-engine:4.12.2")
    }

    configure<JUnitPlatformExtension> {
        platformVersion = "1.0.2"

        filters {
            engines {
                include("spek", "junit-vintage", "junit-jupiter")
            }
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.languageVersion = "1.2"
    }
}
