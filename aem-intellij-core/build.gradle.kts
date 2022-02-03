val kotlinVersion: String by extra
val gsonVersion: String by extra
val apacheCommonsVersion: String by extra

apply {
  plugin("java")
  plugin("kotlin")
}

plugins {
    java
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

repositories {
    mavenCentral()
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
