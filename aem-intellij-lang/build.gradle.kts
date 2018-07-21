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
    val kotlinVersion: String by properties

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
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
    id("org.jetbrains.intellij") version "0.3.4"
}

val kotlinVersion: String by properties

configure<GrammarKitPluginExtension> {
    grammarKitRelease = "1.5.2"
}

java.sourceSets {
    getByName("main").java.srcDirs("src/main/gen")
}

dependencies {
    compile(project(":aem-intellij-common"))

    testCompile(project(":test-framework"))
}

val ideaVersion: String by properties

configure<IntelliJPluginExtension> {
    version = ideaVersion
    setPlugins(
            "IntelliLang", "github", "git4idea"
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

task<GenerateLexer>("generateJpLexer") {
    group = "grammar"
    source = "src/main/flex/JcrPropertyLexer.flex"
    targetDir = "src/main/gen/com/aemtools/lang/jcrproperty"
    targetClass = "_JcrPropertyLexer"
    purgeOldFiles = true
}

task<GenerateLexer>("generateElLexer") {
    group = "grammar"
    source = "src/main/flex/el.flex"
    targetDir = "src/main/gen/com/aemtools/lang/el"
    targetClass = "_ElLexer"
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

task<GenerateParser>("generateJpPsiAndParser") {
    group = "grammar"
    source = "src/main/bnf/jcrproperty.bnf"
    targetRoot = "src/main/gen"
    pathToParser = "/com/aemtools/lang/jcrproperty/JcrPropertyParser.java"
    pathToPsiRoot = "/com/aemtools/lang/jcrproperty/psi"
    purgeOldFiles = true
}

task<GenerateParser>("generateElPsiAndParser") {
    group = "grammar"
    source = "src/main/bnf/el.bnf"
    targetRoot = "src/main/gen"
    pathToParser = "/com/aemtools/lang/el/ElParser.java"
    pathToPsiRoot = "/com/aemtools/lang/el/psi"
    purgeOldFiles = true
}

task("generateGrammar") {
    group = "grammar"
    dependsOn.run {
        add("generateCdLexer")
        add("generateCdPsiAndParser")

        add("generateHtlLexer")
        add("generateHtlPsiAndParser")

        add("generateJpLexer")
        add("generateJpPsiAndParser")

        add("generateElLexer")
        add("generateElPsiAndParser")
    }
}

getTasksByName("compileKotlin", true).first()
        .dependsOn("generateGrammar")

(tasks.find { it.name == "junitPlatformTest" } as JavaExec).apply {
    this.doLast {
        classpath = classpath.plus(files("${rootDir.path}${File.separator}build-log-configs"))
    }
}
