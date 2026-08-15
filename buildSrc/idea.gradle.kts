apply {
  plugin("idea")
}

val copyCodeStyle by tasks.registering(Copy::class) {
  from("$projectDir/config/codeStyleSettings.xml")
  into("$projectDir/.idea")
}

tasks.named("idea") {
  dependsOn(copyCodeStyle)
}
