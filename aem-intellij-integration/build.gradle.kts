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
    plugin("org.junit.platform.gradle.plugin")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.3.4"
}

val contentPackageBuilderVersion: String by properties
val crxPackmgrHelper: String by properties

dependencies {
    compile("com.github.deeprim.aemsync:aemsync-core:2b92cf57c1")

    compile("io.wcm.tooling.commons:io.wcm.tooling.commons.content-package-builder:$contentPackageBuilderVersion")
    compile("io.wcm.tooling.commons:io.wcm.tooling.commons.crx-packmgr-helper:$crxPackmgrHelper")

    compile(project(":aem-intellij-common"))

    testCompile(project(":test-framework"))
}

val ideaVersion: String by properties

configure<IntelliJPluginExtension> {
    version = ideaVersion

    setPlugins(
            "IntelliLang"
    )
}

java.sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
}
