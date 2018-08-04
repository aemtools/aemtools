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
import io.gitlab.arturbosch.detekt.*
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.types.checker.captureFromArguments
import org.junit.platform.console.options.Details

buildscript {
    val kotlinVersion: String by properties

    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }

        dependencies {
            classpath(kotlin("gradle-plugin", kotlinVersion))
            classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.2")
            classpath("com.palantir:jacoco-coverage:0.4.0")
            classpath("gradle.plugin.io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.0.0.RC5-6")
        }
    }
}

val aemtoolsVersion: String by properties

allprojects {
    group = "aemtools"

    version = aemtoolsVersion

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

val kotlinVersion: String by properties
val junitVersion: String by properties
val jmockitVersion: String by properties
val assertjVersion: String by properties
val mockitoVersion: String by properties

plugins {
    java
    id("io.gitlab.arturbosch.detekt").version("1.0.0.RC5-6")
}

apply {
    plugin("com.palantir.jacoco-full-report")
}

subprojects {
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
        mavenLocal()
        jcenter()
        mavenCentral()
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
    }

    val mockitoKotlinVersion: String by properties
    val spekVersion: String by properties
    val junitJupiterApiVersion: String by properties
    val junitJupiterEngineVersion: String by properties
    val junitVintageEngineVersion: String by properties
    val junitPlatformVersion: String by properties

    dependencies {
        compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        compile("org.jetbrains.kotlin:kotlin-runtime:$kotlinVersion")
        compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

        testCompile("com.github.markusbernhardt:proxy-vole:1.0.3")

        testCompile("junit:junit:$junitVersion")
        testCompile("org.jmockit:jmockit:$jmockitVersion")
        testCompile("org.assertj:assertj-core:$assertjVersion")
        testCompile("org.mockito:mockito-core:$mockitoVersion")
        testCompile("com.nhaarman:mockito-kotlin:$mockitoKotlinVersion")

        testCompile("org.jetbrains.spek:spek-api:$spekVersion")
        testRuntime("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion")
        testCompile("org.jetbrains.spek:spek-subject-extension:$spekVersion")

        testCompile("org.junit.jupiter:junit-jupiter-api:$junitJupiterApiVersion")
        testCompile("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
        testCompile("org.junit.jupiter:junit-jupiter-params:$junitJupiterApiVersion")

        testCompile("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
        testCompile("org.junit.platform:junit-platform-console:$junitPlatformVersion")

        testCompile("org.junit.vintage:junit-vintage-engine:$junitVintageEngineVersion")
    }

    // gross patch to address windows "too long classpath" issue
    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        project.apply {
            from("${project.rootProject.projectDir}/buildSrc/win-patch.gradle.kts")
        }
    }

    configure<JUnitPlatformExtension> {
        platformVersion = junitPlatformVersion
        details = Details.TREE
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

task<Wrapper>("wrapper") {
    gradleVersion = "4.9"
    distributionType = Wrapper.DistributionType.ALL
}

configure<JacocoFullReportExtension> {
    excludeProject(":test-framework")
}

detekt {
    version = "1.0.0.RC5-6"
    profile("main", Action {
        input = rootProject.projectDir.absolutePath
        config = "$projectDir/config/detekt.yml"
        filters = "com.aemtools.test.*,.*test.*,"
        parallel = true
    })
}

apply {
    from("buildSrc/idea.gradle.kts")
}
