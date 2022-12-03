import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML

val kotlinVersion: String by extra
val gsonVersion: String by extra
val apacheCommonsVersion: String by extra
val pluginSinceBuild: String by extra
val pluginUntilBuild: String by extra
val pluginVersion: String by extra
val pluginGroup: String by extra
val platformVersion: String by extra
val platformType: String by extra
val platformPlugins: String by extra
fun properties(key: String) = project.findProperty(key).toString()

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
  id("org.jetbrains.changelog")
}

intellij {
  pluginName.set(properties("pluginName"))
  version.set(platformVersion)
  type.set(platformType)
  plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
}

changelog {
  version.set(pluginVersion)
  path.set("${project.parent?.projectDir}/CHANGELOG.md")
  header.set(provider { "[$version] - ${date()}" })
  itemPrefix.set("-")
  keepUnreleasedSection.set(true)
  groups.set(listOf("New features", "Bug fixes", "Maintenance"))
}

dependencies {
  implementation(project(":aem-intellij-common"))
  implementation(project(":aem-intellij-lang"))
  implementation(project(":aem-intellij-inspection"))
  implementation(project(":aem-intellij-index"))

  implementation("com.google.code.gson:gson:$gsonVersion")
  implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")

  testImplementation(project(":test-framework"))
}

tasks {

  patchPluginXml {
    version.set(pluginVersion)
    sinceBuild.set(pluginSinceBuild)
    untilBuild.set(pluginUntilBuild)

    project.parent?.projectDir?.let {
      pluginDescription.set(
        it.resolve("README.md").readText().lines().run {
          val startMarkerText = "<!-- Plugin description -->"
          val endMarkerText = "<!-- Plugin description end -->"

          if (!containsAll(listOf(startMarkerText, endMarkerText))) {
            throw GradleException("Plugin description section not found in README.md:\n$startMarkerText ... $endMarkerText")
          }
          subList(indexOf(startMarkerText) + 1, indexOf(endMarkerText))
        }.joinToString("\n").run { markdownToHTML(this) }
      )
    }
    changeNotes.set(provider { changelog.getLatest().toHTML() })

    pluginXmlFiles.set(fileTree("$projectDir/src/main/resources/META-INF").filter { it.isFile() }.files)
  }

  buildPlugin {
    archiveFileName.set("$pluginGroup-$pluginVersion.zip")

    doLast {
      delete("../build/distributions").also {
        copy {
          from("build/distributions")
          include("*.zip")
          into("../build/distributions")
        }
      }
    }
  }

  runPluginVerifier {
    enabled = true
    subsystemsToCheck.set("without-android")
  }
  listProductsReleases { enabled = true }
  verifyPlugin { enabled = true }

}
