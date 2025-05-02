import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

plugins {
  java
  kotlin("jvm")
  id("org.jetbrains.intellij.platform.module")
  id("org.jetbrains.grammarkit") version "2022.3.2.2"
  id("org.jetbrains.kotlinx.kover")
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

  val generateCdLexer by register<GenerateLexerTask>("generateCdLexer") {
    group = "grammar"
    sourceFile.set(file(file("src/main/flex/Htl.flex")))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/htl/lexer"))
    purgeOldFiles.set(true)
  }

  val generateHtlLexer by register<GenerateLexerTask>("generateHtlLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/_ClientlibDeclarationLexer.flex"))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/clientlib"))
    purgeOldFiles.set(true)
  }

  val generateJpLexer by register<GenerateLexerTask>("generateJpLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/JcrPropertyLexer.flex"))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/jcrproperty"))
    purgeOldFiles.set(true)
  }

  val generateElLexer by register<GenerateLexerTask>("generateElLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/el.flex"))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/el"))
    purgeOldFiles.set(true)
  }

  val generateHtlPsiAndParser by register<GenerateParserTask>("generateHtlPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/Htl.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/htl/HtlParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/htl/psi")
    purgeOldFiles.set(true)

    mustRunAfter(generateHtlLexer)
  }

  val generateCdPsiAndParser by register<GenerateParserTask>("generateCdPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/clientlibdeclaration.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/clientlib/ClientlibDeclarationParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/clientlib/psi")
    purgeOldFiles.set(true)

    mustRunAfter(generateHtlPsiAndParser)
  }

  val generateJpPsiAndParser by register<GenerateParserTask>("generateJpPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/jcrproperty.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/jcrproperty/JcrPropertyParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/jcrproperty/psi")
    purgeOldFiles.set(true)

    mustRunAfter(generateCdPsiAndParser)
  }

  val generateElPsiAndParser by register<GenerateParserTask>("generateElPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/el.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/el/ElParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/el/psi")
    purgeOldFiles.set(true)

    mustRunAfter(generateJpPsiAndParser)
  }

  val generateGrammar by register("generateGrammar") {
    group = "grammar"
    dependsOn(
        generateCdLexer,
        generateHtlLexer,
        generateJpLexer,
        generateElLexer,

        generateCdPsiAndParser,
        generateHtlPsiAndParser,
        generateJpPsiAndParser,
        generateElPsiAndParser
    )
  }

  compileKotlin {
    dependsOn(generateGrammar)
  }

}
