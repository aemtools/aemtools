fun properties(key: String) = project.findProperty(key).toString()
val junitVersion: String by extra
val jmockitVersion: String by extra
val assertjVersion: String by extra
val mockitoVersion: String by extra
val spekVersion: String by extra
val junitJupiterApiVersion: String by extra
val junitJupiterEngineVersion: String by extra

plugins {
    java
    id("org.jetbrains.intellij")
}

apply {
    plugin("java")
    plugin("kotlin")
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
    implementation(project(":aem-intellij-core"))
    implementation(project(":aem-intellij-common"))
    implementation(project(":aem-intellij-lang"))

    implementation("junit:junit:$junitVersion")
    implementation("org.jmockit:jmockit:$jmockitVersion")
    implementation("org.assertj:assertj-core:$assertjVersion")
    implementation("org.mockito:mockito-core:$mockitoVersion")
    implementation("org.jetbrains.spek:spek-api:$spekVersion")

    implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterApiVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
}
