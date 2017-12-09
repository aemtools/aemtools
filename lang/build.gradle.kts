import org.gradle.internal.impldep.org.apache.maven.model.Build
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.grammarkit.GrammarKitPluginExtension
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
        classpath("com.github.hurricup:gradle-grammar-kit-plugin:2017.1.1")
    }

}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
    plugin("org.jetbrains.grammarkit")
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.2.17"
}

val kotlin_version: String by extra

configure<GrammarKitPluginExtension> {
    grammarKitRelease = "1.5.2"
}

java.sourceSets {
    getByName("main").java.srcDirs("src/main/gen")
}

dependencies {
    compile(project(":common"))

    testCompile(project(":test-framework"))
}

val ideaVersion: String by extra

configure<IntelliJPluginExtension> {
    version = ideaVersion
    setPlugins(
            "IntelliLang"
    )
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

task<GenerateLexer>("generateHtlLexer") {
    group = "grammar"
    source = "src/main/flex/Htl.flex"
    targetDir = "src/main/gen/com/aemtools/lang/htl/lexer"
    targetClass = "_HtlLexer"
    purgeOldFiles = true
}

task<GenerateLexer>("generateCdLexer") {
    group = "grammar"
    source = "src/main/flex/_ClientlibDeclarationLexer.flex"
    targetDir = "src/main/gen/com/aemtools/lang/clientlib"
    targetClass = "_ClientlibDeclarationLexer"
    purgeOldFiles = true
}

task<GenerateParser>("generateHtlPsiAndParser") {
    group = "grammar"
    source = "src/main/bnf/Htl.bnf"
    targetRoot = "src/main/gen"
    pathToParser = "/com/aemtools/lang/htl/HtlParser.java"
    pathToPsiRoot = "/com/aemtools/lang/htl/psi"
    purgeOldFiles = true
}

task<GenerateParser>("generateCdPsiAndParser") {
    group = "grammar"
    source = "src/main/bnf/clientlibdeclaration.bnf"
    targetRoot = "src/main/gen"
    pathToParser = "/com/aemtools/lang/clientlib/ClientlibDeclarationParser.java"
    pathToPsiRoot = "/com/aemtools/lang/clientlib/psi"
    purgeOldFiles = true
}

task("generateGrammar") {
    group = "grammar"
    dependsOn.run {
        add("generateCdLexer")
        add("generateCdPsiAndParser")
        add("generateHtlLexer")
        add("generateHtlPsiAndParser")
    }
}

getTasksByName("compileKotlin", true).first()
        .dependsOn("generateGrammar")
