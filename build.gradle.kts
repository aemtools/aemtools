import io.gitlab.arturbosch.detekt.Detekt
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.models.ProductRelease
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = providers.gradleProperty(key).get()
val pluginName = properties("pluginName")
val pluginGroup = properties("pluginGroup")
val pluginVersion = properties("pluginVersion")
val platformVersion = properties("platformVersion")
val platformBundledPlugins = properties("platformBundledPlugins")
val javaVersion = properties("javaVersion")
val kotlinVersion = properties("kotlinVersion")
val rootProjectDirectory = projectDir
val rootProject = project
val pluginSinceBuild = properties("pluginSinceBuild")
val pluginUntilBuild = properties("pluginUntilBuild")
val detektVersion = properties("detektVersion")

plugins {
  id("java")
  kotlin("jvm") version "1.9.20"
  id("org.jetbrains.intellij.platform") version "2.5.0"
  id("org.jetbrains.changelog") version "1.3.1"
  id("io.gitlab.arturbosch.detekt") version "1.23.5"
  id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

group = pluginGroup
version = pluginVersion

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion.toInt())
  targetCompatibility = JavaVersion.toVersion(javaVersion.toInt())

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(javaVersion))
  }
}

intellijPlatform {
  buildSearchableOptions = false
  instrumentCode = true
  pluginConfiguration {
    name = pluginName
    version = pluginVersion

    ideaVersion {
      sinceBuild = pluginSinceBuild
      untilBuild = pluginUntilBuild
    }

    description = providers.fileContents(rootProject.layout.projectDirectory.file("README.md")).asText.map {
      val start = "<!-- Plugin description -->"
      val end = "<!-- Plugin description end -->"

      with(it.lines()) {
        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
      }
    }
    changeNotes = rootProject.changelog.getLatest().toHTML()
  }
  pluginVerification {
    subsystemsToCheck = VerifyPluginTask.Subsystems.ALL
    ides {
      select {
        types.set(listOf(IntelliJPlatformType.IntellijIdeaCommunity))
        channels.set(listOf(ProductRelease.Channel.RELEASE))
        sinceBuild = pluginSinceBuild
        untilBuild = pluginUntilBuild
      }
    }
    failureLevel.set(
        setOf(
            // Temporarily disabled due to https://platform.jetbrains.com/t/plugin-verifier-fails-with-plugin-com-intellij-modules-json-not-declared-as-a-plugin-dependency/580
            // TODO: Uncomment when https://youtrack.jetbrains.com/issue/MP-7366 is fixed
            // VerifyPluginTask.FailureLevel.COMPATIBILITY_PROBLEMS,
            VerifyPluginTask.FailureLevel.INTERNAL_API_USAGES,
            VerifyPluginTask.FailureLevel.INVALID_PLUGIN,
        )
    )
  }
}

changelog {
  version.set(pluginVersion)
  path.set("${project.projectDir}/CHANGELOG.md")
  header.set(provider { "[$version] - ${date()}" })
  itemPrefix.set("-")
  keepUnreleasedSection.set(true)
  groups.set(listOf("New features", "Bug fixes", "Maintenance"))
}

dependencies {
  intellijPlatform {
    intellijIdeaCommunity(platformVersion)
    bundledPlugins(platformBundledPlugins.split(',').map(String::trim).filter(String::isNotEmpty))

    pluginModule(implementation(project(":aem-intellij-common")))
    pluginModule(implementation(project(":aem-intellij-core")))
    pluginModule(implementation(project(":aem-intellij-lang")))
    pluginModule(implementation(project(":aem-intellij-inspection")))
    pluginModule(implementation(project(":aem-intellij-index")))

    testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)

    // Use a specific version of the verifier
    // TODO: remove when https://youtrack.jetbrains.com/issue/MP-7366 is fixed
    // TODO: track updates https://platform.jetbrains.com/t/plugin-verifier-fails-with-plugin-com-intellij-modules-json-not-declared-as-a-plugin-dependency/580
    pluginVerifier(version = "1.383")
  }

  kover(project(":aem-intellij-common"))
  kover(project(":aem-intellij-core"))
  kover(project(":aem-intellij-lang"))
  kover(project(":aem-intellij-index"))
  kover(project(":aem-intellij-inspection"))

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
}

kover {
  currentProject {
    instrumentation {
      disabledForTestTasks.add("test")
    }
  }

  reports {
    total {
      html {
        title = "AEM Tool test coverage merged report"
        onCheck = true
        htmlDir = layout.buildDirectory.dir("merged-report/html")
      }
      xml {
        onCheck = true
        xmlFile = layout.buildDirectory.file("merged-report/xml/report.xml")
      }

      filters {
        excludes {
          classes("generated.psi.impl.*", "com.aemtools.test.*")
        }
      }

      verify {
        rule {
          minBound(80, CoverageUnit.LINE)
        }
      }
    }
  }
}

tasks {
  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  patchPluginXml {
    inputFile.set(file(file("$projectDir/aem-intellij-core/src/main/resources/META-INF/plugin.xml")))
  }
}

allprojects {
  apply {
    plugin("io.gitlab.arturbosch.detekt")
    plugin("java")
  }

  repositories {
    mavenCentral()
  }

  detekt {
    toolVersion = detektVersion
    config.setFrom("$rootProjectDirectory/config/detekt.yml")
    parallel = true
    ignoreFailures = true
    buildUponDefaultConfig = true
    disableDefaultRuleSets = true
    autoCorrect = true
    source.setFrom(files("src/main/java", "src/main/kotlin"))
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
    kotlinOptions.apiVersion = "1.9"
    kotlinOptions.languageVersion = "1.9"
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
    systemProperty("test.java.dir", testJavaDir)

    // uncomment to debug tests
    //systemProperty("idea.log.debug.categories", "com.my.plugin.ui,com.my.plugin.backend")
    //systemProperty("idea.split.test.logs", "true")
  }

  tasks.withType<Detekt>().configureEach {
    jvmTarget = javaVersion
    exclude("com.aemtools.test.*", ".*test.*")

    reports {
      html.required.set(true)
      xml.required.set(true)
      sarif.required.set(true)
      md.required.set(true)
    }
  }

  dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
  }
}

subprojects {
  apply {
    plugin("org.jetbrains.intellij.platform.module")
  }

  repositories {
    mavenCentral()
    intellijPlatform {
      defaultRepositories()
    }
  }

  intellijPlatform {
    buildSearchableOptions = false
  }

  val mockitoKotlinVersion = properties("mockitoKotlinVersion")
  val spekVersion = properties("spekVersion")
  val junit4Version = properties("junit4Version")
  val junitBomVersion = properties("junitBomVersion")
  val assertjVersion = properties("assertjVersion")
  val mockitoVersion = properties("mockitoVersion")

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

    intellijPlatform {
      intellijIdeaCommunity(platformVersion)
      bundledPlugins(platformBundledPlugins.split(',').map(String::trim).filter(String::isNotEmpty))

      testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
      testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)

      // Use a specific version of the verifier
      // TODO: remove when https://youtrack.jetbrains.com/issue/MP-7366 is fixed
      // TODO: track updates https://platform.jetbrains.com/t/plugin-verifier-fails-with-plugin-com-intellij-modules-json-not-declared-as-a-plugin-dependency/580
      pluginVerifier(version = "1.383")
    }
  }
}

apply {
  from("buildSrc/idea.gradle.kts")
}
