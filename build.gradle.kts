import dev.detekt.gradle.Detekt
import org.jetbrains.changelog.Changelog
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.Constants
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.gradle.api.tasks.JavaExec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = providers.gradleProperty(key).get()
fun csvProperty(key: String) = properties(key).split(',').map(String::trim).filter(String::isNotEmpty)
val pluginName = properties("pluginName")
val pluginGroup = properties("pluginGroup")
val pluginVersion = properties("pluginVersion")
val platformType = properties("platformType")
val platformVersion = properties("platformVersion")
val platformPlugins = csvProperty("platformPlugins")
val platformBundledPlugins = csvProperty("platformBundledPlugins")
val platformBundledModules = csvProperty("platformBundledModules")
val javaLanguageLevel = properties("javaVersion")
val rootProjectDirectory = projectDir
val rootProject = project
val pluginSinceBuild = properties("pluginSinceBuild")
val pluginUntilBuild = properties("pluginUntilBuild")

plugins {
  id("java")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatform)
  alias(libs.plugins.changelog)
  alias(libs.plugins.detekt)
  alias(libs.plugins.kover)
  alias(libs.plugins.qodana)
}

val detektToolVersion = libs.versions.detekt.get()
val detektKtlintWrapperDependency = libs.detekt.ktlint.wrapper
val assertjDependency = libs.assertj
val mockitoCoreDependency = libs.mockito.core
val mockitoKotlinDependency = libs.mockito.kotlin
val junitBomDependency = libs.junit.bom
val junitJupiterDependency = libs.junit.jupiter
val junit4Dependency = libs.junit
val junitVintageEngineDependency = libs.junit.vintage.engine
val junitPlatformSuiteDependency = libs.junit.platform.suite
val junitPlatformLauncherDependency = libs.junit.platform.launcher
val junitPlatformConsoleDependency = libs.junit.platform.console
val spekApiDependency = libs.spek.api
val spekJunitPlatformEngineDependency = libs.spek.junit.platform.engine
val spekSubjectExtensionDependency = libs.spek.subject.extension

group = pluginGroup
version = pluginVersion

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

java {
  sourceCompatibility = JavaVersion.toVersion(javaLanguageLevel.toInt())
  targetCompatibility = JavaVersion.toVersion(javaLanguageLevel.toInt())

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(javaLanguageLevel))
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
    val changelog = project.changelog
    val pluginVersion = providers.gradleProperty("pluginVersion").get()
    changeNotes = provider {
      changelog.renderItem(
          (changelog.getOrNull(pluginVersion) ?: changelog.getUnreleased())
              .withHeader(false)
              .withEmptySections(false),
          Changelog.OutputType.HTML,
      )
    }
  }
  pluginVerification {
    subsystemsToCheck = VerifyPluginTask.Subsystems.ALL
    ides {
      recommended()
      /*select {
        types.set(listOf(IntelliJPlatformType.IntellijIdeaCommunity))
        channels.set(listOf(ProductRelease.Channel.RELEASE))
        sinceBuild = pluginSinceBuild
        untilBuild = pluginUntilBuild
      }*/
    }
    failureLevel.set(
        setOf(
            // Temporarily disabled due to https://platform.jetbrains.com/t/plugin-verifier-fails-with-plugin-com-intellij-modules-json-not-declared-as-a-plugin-dependency/580
            // TODO: Uncomment when https://youtrack.jetbrains.com/issue/MP-7366 is fixed
            VerifyPluginTask.FailureLevel.COMPATIBILITY_PROBLEMS,
            VerifyPluginTask.FailureLevel.INTERNAL_API_USAGES,
            VerifyPluginTask.FailureLevel.INVALID_PLUGIN,
        )
    )
  }

  signing {
    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    privateKey = providers.environmentVariable("PRIVATE_KEY")
    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
  }

  publishing {
    token = providers.environmentVariable("PUBLISH_TOKEN")
    channels = providers.gradleProperty("pluginVersion").map {
      listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" })
    }
  }
}

changelog {
  version = pluginVersion
  groups = listOf("New features", "Bug fixes", "Maintenance")
}

dependencies {
  intellijPlatform {
    create(platformType, platformVersion)

    bundledModules(platformBundledModules)
    bundledPlugins(platformBundledPlugins)
    plugins(platformPlugins)

    pluginModule(implementation(project(":aem-intellij-common")))
    pluginModule(implementation(project(":aem-intellij-core")))
    pluginModule(implementation(project(":aem-intellij-lang")))
    pluginModule(implementation(project(":aem-intellij-inspection")))
    pluginModule(implementation(project(":aem-intellij-index")))

    testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    testFramework(TestFrameworkType.JUnit5, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
    javaCompiler()

    // Use a specific version of the verifier
    // TODO: remove when https://youtrack.jetbrains.com/issue/MP-7366 is fixed
    // TODO: track updates https://platform.jetbrains.com/t/plugin-verifier-fails-with-plugin-com-intellij-modules-json-not-declared-as-a-plugin-dependency/580
    //pluginVerifier(version = "1.383")
  }

  kover(project(":aem-intellij-common"))
  kover(project(":aem-intellij-core"))
  kover(project(":aem-intellij-lang"))
  kover(project(":aem-intellij-index"))
  kover(project(":aem-intellij-inspection"))

  detektPlugins(detektKtlintWrapperDependency)
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
  processResources {
    from("aem-intellij-core/src/main/resources/META-INF") {
      include("*.xml")
      exclude("plugin.xml")
      into("META-INF")
    }
  }

  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  publishPlugin {
    dependsOn(patchChangelog)
  }

  patchPluginXml {
    inputFile.set(file(file("$projectDir/aem-intellij-core/src/main/resources/META-INF/plugin.xml")))
  }
}

allprojects {
  apply {
    plugin("dev.detekt")
    plugin("java")
  }

  repositories {
    mavenCentral()
  }

  detekt {
    toolVersion = detektToolVersion
    config.setFrom("$rootProjectDirectory/config/detekt.yml")
    parallel = true
    ignoreFailures = true
    buildUponDefaultConfig = true
    disableDefaultRuleSets = true
    autoCorrect = true
    source.setFrom(files("src/main/java", "src/main/kotlin"))
  }

  plugins.withId("org.jetbrains.kotlinx.kover") {
    kover {
      currentProject {
        instrumentation {
          excludedClasses.addAll(
              "com.intellij.debugger.*",
              "com.intellij.platform.debugger.*",
              "org.jetbrains.kotlin.idea.debugger.*",
          )
        }
      }
    }
  }

  java {
    sourceCompatibility = JavaVersion.toVersion(javaLanguageLevel.toInt())
    targetCompatibility = JavaVersion.toVersion(javaLanguageLevel.toInt())

    toolchain {
      languageVersion.set(JavaLanguageVersion.of(javaLanguageLevel))
    }
  }

  tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaLanguageLevel.toInt())
    sourceCompatibility = javaLanguageLevel
    targetCompatibility = javaLanguageLevel

    javaCompiler.set(javaToolchains.compilerFor {
      languageVersion.set(JavaLanguageVersion.of(javaLanguageLevel))
    })
  }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(JvmTarget.fromTarget(javaLanguageLevel))
      apiVersion.set(KotlinVersion.fromVersion("2.4"))
      languageVersion.set(KotlinVersion.fromVersion("2.4"))
    }
  }

  tasks.withType<JavaExec>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor {
      languageVersion.set(JavaLanguageVersion.of(javaLanguageLevel))
    })
  }

  tasks.withType<Test>().configureEach {
    jvmArgs("--add-modules=jdk.jdi")

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
    jvmTarget.set(javaLanguageLevel)
    exclude("com.aemtools.test.*", ".*test.*")

    reports {
      html.required.set(true)
      checkstyle.required.set(true)
      sarif.required.set(true)
      markdown.required.set(true)
    }
  }

  dependencies {
    detektPlugins(detektKtlintWrapperDependency)
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
    testImplementation(assertjDependency)
    testImplementation(mockitoCoreDependency)
    testImplementation(mockitoKotlinDependency)

    // Use junit-bom to align versions
    // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
    implementation(platform(junitBomDependency)) {
      because("Platform, Jupiter, and Vintage versions should match")
    }

    // JUnit Jupiter
    testImplementation(junitJupiterDependency)

    // JUnit Vintage
    testImplementation(junit4Dependency)
    testRuntimeOnly(junitVintageEngineDependency) {
      because("allows JUnit 3 and JUnit 4 tests to run")
    }

    // JUnit Suites
    testImplementation(junitPlatformSuiteDependency)

    // JUnit Platform Launcher + Console
    testRuntimeOnly(junitPlatformLauncherDependency) {
      because("allows tests to run from IDEs that bundle older version of launcher")
    }
    testRuntimeOnly(junitPlatformConsoleDependency) {
      because("needed to launch the JUnit Platform Console program")
    }

    testImplementation(spekApiDependency) {
      exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly(spekJunitPlatformEngineDependency) {
      exclude(group = "org.jetbrains.kotlin")
      exclude(group = "org.junit.platform")
    }
    testImplementation(spekSubjectExtensionDependency) {
      exclude(group = "org.jetbrains.kotlin")
      exclude(group = "org.junit.platform")
    }

    intellijPlatform {
      create(platformType, platformVersion)
      bundledModules(platformBundledModules)
      bundledPlugins(platformBundledPlugins)
      plugins(platformPlugins)

      testFramework(TestFrameworkType.Platform, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
      testFramework(TestFrameworkType.Plugin.Java, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
      testFramework(TestFrameworkType.JUnit5, configurationName = Constants.Configurations.INTELLIJ_PLATFORM_DEPENDENCIES)
      javaCompiler()

      // Use a specific version of the verifier
      // TODO: remove when https://youtrack.jetbrains.com/issue/MP-7366 is fixed
      // TODO: track updates https://platform.jetbrains.com/t/plugin-verifier-fails-with-plugin-com-intellij-modules-json-not-declared-as-a-plugin-dependency/580
      //pluginVerifier(version = "1.383")
    }
  }
}

apply {
  from("buildSrc/idea.gradle.kts")
}
