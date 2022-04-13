import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij")
  id("org.jetbrains.grammarkit") version "2021.2.1"
}

buildscript {

  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }

  dependencies {
    classpath("org.jetbrains.intellij.plugins:gradle-grammarkit-plugin:2021.2.1")
  }
}

grammarKit {
  // Version of IntelliJ patched JFlex (see the link below), Default is 1.7.0-1
  jflexRelease.set("1.7.0-1")
}

dependencies {
  implementation(project(":aem-intellij-common"))

  testImplementation(project(":test-framework"))
}

configure<SourceSetContainer> {
  val main by getting
  main.java.srcDirs("src/main/gen")
}

tasks {

  task<GenerateLexerTask>("generateCdLexer") {
    group = "grammar"
    source.set("src/main/flex/Htl.flex")
    targetDir.set("src/main/gen/com/aemtools/lang/htl/lexer")
    targetClass.set("_HtlLexer")
    purgeOldFiles.set(true)
  }

  task<GenerateLexerTask>("generateHtlLexer") {
    group = "grammar"
    source.set("src/main/flex/_ClientlibDeclarationLexer.flex")
    targetDir.set("src/main/gen/com/aemtools/lang/clientlib")
    targetClass.set("_ClientlibDeclarationLexer")
    purgeOldFiles.set(true)
  }

  task<GenerateLexerTask>("generateJpLexer") {
    group = "grammar"
    source.set("src/main/flex/JcrPropertyLexer.flex")
    targetDir.set("src/main/gen/com/aemtools/lang/jcrproperty")
    targetClass.set("_JcrPropertyLexer")
    purgeOldFiles.set(true)
  }

  task<GenerateLexerTask>("generateElLexer") {
    group = "grammar"
    source.set("src/main/flex/el.flex")
    targetDir.set("src/main/gen/com/aemtools/lang/el")
    targetClass.set("_ElLexer")
    purgeOldFiles.set(true)
  }

  task<GenerateParserTask>("generateHtlPsiAndParser") {
    group = "grammar"
    source.set("src/main/bnf/Htl.bnf")
    targetRoot.set("src/main/gen")
    pathToParser.set("/com/aemtools/lang/htl/HtlParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/htl/psi")
    purgeOldFiles.set(true)
  }

  task<GenerateParserTask>("generateCdPsiAndParser") {
    group = "grammar"
    source.set("src/main/bnf/clientlibdeclaration.bnf")
    targetRoot.set("src/main/gen")
    pathToParser.set("/com/aemtools/lang/clientlib/ClientlibDeclarationParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/clientlib/psi")
    purgeOldFiles.set(true)
  }

  task<GenerateParserTask>("generateJpPsiAndParser") {
    group = "grammar"
    source.set("src/main/bnf/jcrproperty.bnf")
    targetRoot.set("src/main/gen")
    pathToParser.set("/com/aemtools/lang/jcrproperty/JcrPropertyParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/jcrproperty/psi")
    purgeOldFiles.set(true)
  }

  task<GenerateParserTask>("generateElPsiAndParser") {
    group = "grammar"
    source.set("src/main/bnf/el.bnf")
    targetRoot.set("src/main/gen")
    pathToParser.set("/com/aemtools/lang/el/ElParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/el/psi")
    purgeOldFiles.set(true)
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
}
