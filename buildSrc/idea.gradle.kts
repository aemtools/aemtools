import org.gradle.plugins.ide.idea.model.IdeaWorkspace

apply {
  plugin("idea")
}

task<Copy>("copyCodeStyle") {
  from("$projectDir/config/codeStyleSettings.xml")
  into("$projectDir/.idea")
}

tasks["idea"].dependsOn(tasks["copyCodeStyle"])
