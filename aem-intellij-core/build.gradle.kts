val kotlinVersion: String by extra
val gsonVersion: String by extra
val apacheCommonsVersion: String by extra
val pluginSinceBuild: String by extra
val pluginUntilBuild: String by extra
val pluginVersion: String by extra
val platformVersion: String by extra
val platformType: String by extra
val platformPlugins: String by extra
fun properties(key: String) = project.findProperty(key).toString()

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
}

intellij {
  pluginName.set(properties("pluginName"))
  version.set(platformVersion)
  type.set(platformType)
  plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
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
    /*changeNotes.set(provider {
      changelog.run {
        getOrNull(pluginVersion) ?: getLatest()
      }.toHTML()
    })*/
    pluginXmlFiles.set(fileTree("$projectDir/src/main/resources/META-INF").filter { it.isFile() }.files)
  }
}
