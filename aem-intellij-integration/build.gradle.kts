fun properties(key: String) = project.findProperty(key).toString()

plugins {
  id("org.jetbrains.intellij")
}

buildscript {
  val kotlinVersion: String by extra

  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(kotlin("gradle-plugin", kotlinVersion))
  }
}
apply {
  plugin("java")
  plugin("kotlin")
  plugin("org.jetbrains.intellij")
}

dependencies {
  implementation("com.github.deeprim.aemsync:aemsync-core:2b92cf57c1")

  implementation("io.wcm.tooling.commons:io.wcm.tooling.commons.content-package-builder:$contentPackageBuilderVersion")
  implementation("io.wcm.tooling.commons:io.wcm.tooling.commons.crx-packmgr-helper:$crxPackmgrHelper")

  implementation(project(":aem-intellij-common"))

  testimplementation(project(":test-framework"))
}

java.sourceSets {
  getByName("main").java.srcDirs("src/main/kotlin")
}
