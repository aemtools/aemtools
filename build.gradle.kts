import io.gitlab.arturbosch.detekt.Detekt
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = providers.gradleProperty(key).get()
val pluginName = properties("pluginName")
val pluginGroup = properties("pluginGroup")
val pluginVersion = properties("pluginVersion")
val platformType = properties("platformType")
val platformVersion = properties("platformVersion")
val platformBundledPlugins = properties("platformBundledPlugins")
val javaVersion = libs.versions.java.get()
val rootProjectDirectory = projectDir
val rootProject = project
val pluginSinceBuild = properties("pluginSinceBuild")
val pluginUntilBuild = properties("pluginUntilBuild")
val detektVersion = libs.versions.detekt.get()

plugins {
  id("java")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatform)
  alias(libs.plugins.changelog)
  alias(libs.plugins.detekt)
  alias(libs.plugins.kover)
  alias(libs.plugins.powerAssert)
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
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(javaVersion))
  }
}

kotlin {
  jvmToolchain(javaVersion.toInt())
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
    val changelog = rootProject.changelog // local variable for configuration cache compatibility
    // Get the latest available change notes from the changelog file
    changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
      with(changelog) {
        renderItem(
            (getOrNull(pluginVersion) ?: getUnreleased())
                .withHeader(false)
                .withEmptySections(false),
            Changelog.OutputType.HTML,
        )
      }
    }
  }
  pluginVerification {
    subsystemsToCheck = VerifyPluginTask.Subsystems.ALL
    ides {
      recommended()
    }
    failureLevel.set(
        setOf(
            VerifyPluginTask.FailureLevel.COMPATIBILITY_PROBLEMS,
            VerifyPluginTask.FailureLevel.INTERNAL_API_USAGES,
            VerifyPluginTask.FailureLevel.INVALID_PLUGIN,
        )
    )
  }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
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
    create(platformType, platformVersion)

    bundledPlugins(platformBundledPlugins.split(',').map(String::trim).filter(String::isNotEmpty))

    pluginComposedModule(implementation(project(":aem-intellij-common")))
    pluginComposedModule(implementation(project(":aem-intellij-core")))
    pluginComposedModule(implementation(project(":aem-intellij-lang")))
    pluginComposedModule(implementation(project(":aem-intellij-inspection")))
    pluginComposedModule(implementation(project(":aem-intellij-index")))

    testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
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
    compilerOptions {
      this.jvmTarget.set(JvmTarget.fromTarget(javaVersion))
    }
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

  dependencies {
    intellijPlatform {
      create(platformType, platformVersion)
      bundledPlugins(platformBundledPlugins.split(',').map(String::trim).filter(String::isNotEmpty))

      testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
      testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    }
  }
}

apply {
  from("buildSrc/idea.gradle.kts")
}
