import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.jetbrains.intellij") version "0.7.2"
}

repositories {
    mavenCentral()
}

val junitVersion: String by project
val jmockitVersion: String by project
val assertjVersion: String by project
val mockitoVersion: String by project
val spekVersion: String by project
val junitJupiterApiVersion: String by project
val junitJupiterEngineVersion: String by project

dependencies {
  implementation(project(":aem-intellij-core"))
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))

  implementation("junit:junit:$junitVersion")
  implementation("org.jmockit:jmockit:$jmockitVersion")
  implementation("org.assertj:assertj-core:$assertjVersion")
  implementation("org.mockito:mockito-core:$mockitoVersion")
  implementation("org.jetbrains.spek:spek-api:$spekVersion")

  implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterApiVersion")
  implementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
}

val ideaVersion: String by project

intellij {
  version = ideaVersion
  setPlugins("IntelliLang", "java")
}
