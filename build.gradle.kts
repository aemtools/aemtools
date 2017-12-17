import com.palantir.jacoco.JacocoFullReportExtension
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.wrapper.GradleWrapperMain

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
            classpath("com.palantir:jacoco-coverage:0.4.0")
        }
    }
}

val aemtools_version: String by extra

allprojects {
    group = "aemtools"

    version = aemtools_version

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

apply {
    plugin("com.palantir.jacoco-full-report")
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
            sourceDirectories = files("$projectDir/src/main/kotlin")
            classDirectories = files("$buildDir/classes/kotlin/main")
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

    val mockito_kotlin_version: String by extra
    val spek_version: String by extra
    val junit_jupiter_api_version: String by extra
    val junit_jupiter_engine_version: String by extra
    val junit_vintage_engine_version: String by extra

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
        compile("org.jetbrains.kotlin:kotlin-runtime:$kotlin_version")
        compile("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
        compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

        testCompile("com.github.markusbernhardt:proxy-vole:1.0.3")

        testCompile("junit:junit:$junit_version")
        testCompile("org.jmockit:jmockit:$jmockit_version")
        testCompile("org.assertj:assertj-core:$assertj_version")
        testCompile("org.mockito:mockito-core:$mockito_version")
        testCompile("com.nhaarman:mockito-kotlin:$mockito_kotlin_version")

        testCompile("org.jetbrains.spek:spek-api:$spek_version")
        testRuntime("org.jetbrains.spek:spek-junit-platform-engine:$spek_version")
        testCompile("org.jetbrains.spek:spek-subject-extension:$spek_version")
        testCompile("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_api_version")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_engine_version")
        testRuntime("org.junit.vintage:junit-vintage-engine:$junit_vintage_engine_version")
    }

    val junit_platform_version: String by extra
    configure<JUnitPlatformExtension> {
        platformVersion = junit_platform_version

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

task<Wrapper>("gradleWrapper") {
    gradleVersion = "4.4"
}

configure<JacocoFullReportExtension> {
    excludeProject(":test-framework")
}
