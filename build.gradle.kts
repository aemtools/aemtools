import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()
val pluginName: String by extra
val pluginGroup: String by extra
val pluginVersion: String by extra
val platformVersion: String by extra
val platformType: String by extra
val platformPlugins: String by extra
val javaVersion: String by extra
val kotlinVersion: String by extra
val rootProjectDirectory = projectDir
val rootProject = project

plugins {
  id("java")
  kotlin("jvm") version "1.6.10"
  id("org.jetbrains.intellij") version "1.10.0"
  id("org.jetbrains.changelog") version "1.3.1" apply false
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

group = pluginGroup
version = pluginVersion

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion.toInt())
  targetCompatibility = JavaVersion.toVersion(javaVersion.toInt())

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(javaVersion))
  }
}

intellij {
  pluginName.set(properties("pluginName"))
  version.set(platformVersion)
  type.set(platformType)
  plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
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
    htmlReportDir.set(layout.buildDirectory.dir("merged-report/html"))

    //includes = listOf("com.aemtools.*")
    excludes = listOf("generated.psi.impl.*", "com.aemtools.test.*")
  }

  koverMergedXmlReport {
    isEnabled = true
    excludes = listOf("generated.psi.impl.*")
  }

  koverMergedVerify {
    isEnabled = false
    excludes = listOf("generated.psi.impl.*")
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
  }

  runIde {
    configDir.set(file("${project(":aem-intellij-core").buildDir}/idea-sandbox/config"))
    pluginsDir.set(file("${project(":aem-intellij-core").buildDir}/idea-sandbox/plugins"))
    systemDir.set(file("${project(":aem-intellij-core").buildDir}/idea-sandbox/system"))
  }

  buildSearchableOptions { enabled = false }
  runPluginVerifier { enabled = false }
  listProductsReleases { enabled = false }
  verifyPlugin { enabled = false }
}

buildscript {

  repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }

    dependencies {
      classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
    }
  }
}

allprojects {
  apply {
    plugin("io.gitlab.arturbosch.detekt")
    plugin("java")
  }

  repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
  }

  detekt {
    toolVersion = "1.19.0"
    config = files("$rootProjectDirectory/config/detekt.yml")
    parallel = true
    ignoreFailures = true
    buildUponDefaultConfig = true
    disableDefaultRuleSets = true
    autoCorrect = true
    source = files("src/main/java", "src/main/kotlin")
  }

  java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion.toInt())
    targetCompatibility = JavaVersion.toVersion(javaVersion.toInt())

    toolchain {
      languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
  }

  tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersion.toInt())
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    javaCompiler.set(javaToolchains.compilerFor {
      languageVersion.set(JavaLanguageVersion.of(javaVersion))
    })
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = javaVersion
    kotlinOptions.apiVersion = "1.6"
    kotlinOptions.languageVersion = "1.6"
  }

  tasks.withType<Test>().configureEach {
    useJUnitPlatform {
      includeEngines("spek", "junit-vintage", "junit-jupiter")
    }
    testLogging {
      events("standardOut", "passed", "skipped", "failed")
      showStandardStreams = true
    }

    val testJavaDir = rootProject.allprojects.first {
      it.name == "test-framework"
    }.projectDir.absolutePath + "/src/main/resources/java"
    logger.lifecycle("Test java directory: $testJavaDir")
    systemProperty("test.java.dir", testJavaDir)
  }

  tasks.withType<Detekt>().configureEach {
    exclude("com.aemtools.test.*", ".*test.*")
  }
}

subprojects {
  apply {
    plugin("org.jetbrains.intellij")
  }

  repositories {
    mavenCentral()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
  }

  intellij {
    pluginName.set(properties("pluginName"))
    version.set(platformVersion)
    type.set(platformType)
    plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
  }

  tasks {
    buildSearchableOptions { enabled = false }
    runPluginVerifier { enabled = false }
    listProductsReleases { enabled = false }
    verifyPlugin { enabled = false }
  }

  val kotlinVersion: String by extra
  val mockitoKotlinVersion: String by extra
  val spekVersion: String by extra
  val junit4Version: String by extra
  val junitBomVersion: String by extra
  val assertjVersion: String by extra
  val mockitoVersion: String by extra

  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")

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
    testRuntimeOnly("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion") {
      exclude(group = "org.jetbrains.kotlin")
      exclude(group = "org.junit.platform")
    }
    testImplementation("org.jetbrains.spek:spek-subject-extension:$spekVersion") {
      exclude(group = "org.jetbrains.kotlin")
      exclude(group = "org.junit.platform")
    }

  }

  // gross patch to address windows "too long classpath" issue
  /*if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    project.apply {
      from("${project.rootProject.projectDir}/buildSrc/win-patch.gradle.kts")
    }
  }*/
}

apply {
  from("buildSrc/idea.gradle.kts")
}
