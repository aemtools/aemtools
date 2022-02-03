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
  id("org.jetbrains.kotlin.jvm") version "1.6.0"
  id("org.jetbrains.intellij") version "1.3.0"
  id("org.jetbrains.changelog") version "1.3.1"
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  id("jacoco")
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

tasks {
  properties("javaVersion").let {
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = it
    }
  }

  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  patchPluginXml {
    version.set(pluginVersion)
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
    /*pluginDescription.set(
        projectDir.resolve("README.md").readText().lines().run {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            if (!containsAll(listOf(start, end))) {
                throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
            }
            subList(indexOf(start) + 1, indexOf(end))
        }.joinToString("\n").run { markdownToHTML(this) }
    )*/

    // Get the latest available change notes from the changelog file
    changeNotes.set(provider {
      changelog.run {
        getOrNull(pluginVersion) ?: getLatest()
      }.toHTML()
    })
  }
  buildSearchableOptions {
    jbrVersion.set("11_0_2b159")
  }
}

buildscript {
  val kotlinVersion: String by extra

  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }

    dependencies {
      classpath(kotlin("gradle-plugin", kotlinVersion))
      //classpath("org.junit.platform:junit-platform-gradle-plugin:1.2.0")
      classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
    }
  }
}

allprojects {
  group = pluginGroup
  version = pluginVersion

  apply {
    plugin("io.gitlab.arturbosch.detekt")
    plugin("org.jetbrains.intellij")
    //plugin("org.junit.platform.gradle.plugin")
  }

  repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }

  detekt {
    version = "1.19.0"
    config = files("$projectDirectory/config/detekt.yml")
    parallel = true
    ignoreFailures = true
    buildUponDefaultConfig = true
    disableDefaultRuleSets = true
    autoCorrect = true
    source = files("src/main/java", "src/main/kotlin")
  }

  tasks.withType<Detekt>().configureEach {
    exclude("com.aemtools.test.*", ".*test.*")
  }
}

subprojects {
  apply {
    plugin("java")
    plugin("kotlin")
    plugin("jacoco")
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
  val spekVersion: String by extra
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
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    //testImplementation("com.github.markusbernhardt:proxy-vole:1.0.3")

    testImplementation("junit:junit:$junitVersion")
    testImplementation("org.jmockit:jmockit:$jmockitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")

    testImplementation("org.jetbrains.spek:spek-api:$spekVersion")
    testRuntimeOnly("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion")
    testImplementation("org.jetbrains.spek:spek-subject-extension:$spekVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterApiVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterApiVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-console:1.2.0")

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVintageEngineVersion")
  }

  jacoco {
    toolVersion = jacocoVersion
    reportsDirectory.set(file("$buildDir/jacocoReport"))
  }

  // gross patch to address windows "too long classpath" issue
  if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    project.apply {
      from("${project.rootProject.projectDir}/buildSrc/win-patch.gradle.kts")
    }
  }

  afterEvaluate {
    tasks.jacocoTestReport {
      dependsOn(tasks.test)
      sourceDirectories.setFrom(files("$projectDir/src/main/kotlin"))
      classDirectories.setFrom(files("$buildDir/classes/kotlin/main"))
      reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/jacocoReport/xml/jacocoTestReport.xml"))
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/jacocoReport/html"))
      }
    }
  }

  tasks {
    properties("javaVersion").let {
      withType<JavaCompile> {
        sourceCompatibility = it
        targetCompatibility = it
      }
      withType<KotlinCompile> {
        kotlinOptions.jvmTarget = it
      }
    }

    test {
      useJUnitPlatform {
        includeEngines.addAll(listOf("spek", "junit-vintage", "junit-jupiter"))
      }
    }
  }
}

apply {
  from("buildSrc/idea.gradle.kts")
}
