import io.gitlab.arturbosch.detekt.Detekt
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()
val pluginName: String by extra
val pluginGroup: String by extra
val pluginVersion: String by extra
val platformVersion: String by extra
val platformType: String by extra
val platformPlugins: String by extra
val projectDirectory = getProjectDir()

plugins {
  id("java")
  kotlin("jvm") version "1.5.32"
  id("org.jetbrains.intellij") version "1.5.2"
  id("org.jetbrains.changelog") version "1.3.1"
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

group = pluginGroup
version = pluginVersion

repositories {
  mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
  pluginName.set(properties("pluginName"))
  version.set(platformVersion)
  type.set(platformType)

  plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
}

changelog {
  version.set(pluginVersion)
  groups.set(emptyList())
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

kover {
  isDisabled = false
  coverageEngine.set(kotlinx.kover.api.CoverageEngine.INTELLIJ)
  //coverageEngine.set(kotlinx.kover.api.CoverageEngine.JACOCO)
  intellijEngineVersion.set("1.0.656")
  jacocoEngineVersion.set("0.8.8")
  runAllTestsForProjectTask = true
  generateReportOnCheck = true
}

tasks {

  koverMergedHtmlReport {
    isEnabled = true
    htmlReportDir.set(layout.buildDirectory.dir("my-merged-report/html-result"))

    //includes = listOf("com.aemtools.*")
    excludes = listOf("generated.psi.impl.*","com.aemtools.test.*")
  }

  koverMergedXmlReport {
    isEnabled = true
    excludes = listOf("generated.psi.impl.*")
  }

  koverMergedVerify {
    isEnabled = false
    //includes = listOf("com.aemtools.*")
    //excludes = listOf("generated.psi.impl.*")
    rule {
      name = "Minimal line coverage rate in percent"
      bound {
        minValue = 80
      }
    }
  }

  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  buildSearchableOptions {
    enabled = false
    jbrVersion.set("11_0_2b159")
  }
}

buildscript {

  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }

    dependencies {
      classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
    }
  }
}

allprojects {
  group = pluginGroup
  version = pluginVersion

  apply {
    plugin("io.gitlab.arturbosch.detekt")
    plugin("java")
  }

  repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }

  detekt {
    toolVersion = "1.19.0"
    config = files("$projectDirectory/config/detekt.yml")
    parallel = true
    ignoreFailures = true
    buildUponDefaultConfig = true
    disableDefaultRuleSets = true
    autoCorrect = true
    source = files("src/main/java", "src/main/kotlin")
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    toolchain {
      languageVersion.set(JavaLanguageVersion.of(11))
    }
  }

  tasks.withType<Test>().configureEach {
    useJUnitPlatform {
      includeEngines("spek", "junit-vintage", "junit-jupiter")
    }
    //useJUnitPlatform()
    testLogging {
      events("standardOut", "passed", "skipped", "failed", "STANDARD_OUT", "STANDARD_ERROR")
      showStandardStreams = true
    }
  }

  tasks.withType<Detekt>().configureEach {
    exclude("com.aemtools.test.*", ".*test.*")
  }

  tasks.withType<JavaCompile>().configureEach {
    options.release.set(11)
    sourceCompatibility = "11"
    targetCompatibility = "11"
    options.encoding = "UTF-8"

    javaCompiler.set(javaToolchains.compilerFor {
      languageVersion.set(JavaLanguageVersion.of(11))
    })
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.apiVersion = "1.5"
    kotlinOptions.languageVersion = "1.5"
  }

}

subprojects {
  apply {
    plugin("org.jetbrains.intellij")
  }

  repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }

  intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
  }

  val kotlinVersion: String by extra
  val mockitoKotlinVersion: String by extra
  val spekVersion = "1.1.5"
  var junit5Version = "5.8.2"
  val junitJupiterApiVersion: String by extra
  val junitJupiterEngineVersion: String by extra
  val junitVintageEngineVersion: String by extra
  val junitPlatformVersion: String by extra
  val junitVersion: String by extra
  val jmockitVersion: String by extra
  val assertjVersion: String by extra
  val mockitoVersion: String by extra
  val jacocoVersion: String by extra

  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")

    val junit4Version = "4.13.2"
    val junitBomVersion = "5.8.2"

    // Use junit-bom to align versions
    // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
    implementation(platform("org.junit:junit-bom:$junitBomVersion")) {
      because("Platform, Jupiter, and Vintage versions should match")
    }

    // JUnit Jupiter
    testImplementation("org.junit.jupiter:junit-jupiter")

    // JUnit Vintage
    testImplementation("junit:junit:$junit4Version")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine") {
      because("allows JUnit 3 and JUnit 4 tests to run")
    }

    // JUnit Suites
    testImplementation("org.junit.platform:junit-platform-suite")

    // JUnit Platform Launcher + Console
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
      because("allows tests to run from IDEs that bundle older version of launcher")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-console") {
      because("needed to launch the JUnit Platform Console program")
    }

    testImplementation("org.jetbrains.spek:spek-api:$spekVersion") {
      exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion"){
      exclude(group = "org.jetbrains.kotlin")
      exclude(group = "org.junit.platform")
    }
    testImplementation("org.jetbrains.spek:spek-subject-extension:$spekVersion") {
      exclude(group = "org.jetbrains.kotlin")
      exclude(group = "org.junit.platform")
    }

  }

  // gross patch to address windows "too long classpath" issue
  if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    project.apply {
      from("${project.rootProject.projectDir}/buildSrc/win-patch.gradle.kts")
    }
  }
}

apply {
  from("buildSrc/idea.gradle.kts")
}
