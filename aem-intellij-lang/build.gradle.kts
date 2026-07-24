import org.jetbrains.intellij.platform.gradle.tasks.GenerateLexerTask
import org.jetbrains.intellij.platform.gradle.tasks.GenerateParserTask

plugins {
  java
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.intellij.platform.module")
  id("org.jetbrains.intellij.platform.grammarkit")
  id("org.jetbrains.kotlinx.kover")
}

dependencies {
  implementation(project(":aem-intellij-common"))

  testImplementation(project(":test-framework"))
  testImplementation(libs.kotlinx.coroutines.test) {
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-bom")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
  }
}

configure<SourceSetContainer> {
  val main by getting
  main.java.srcDirs("src/main/gen")
}

tasks {

  val generateCdLexer by register<GenerateLexerTask>("generateCdLexer") {
    group = "grammar"
    sourceFile.set(file(file("src/main/flex/Htl.flex")))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToClass.set("/com/aemtools/lang/htl/lexer/_HtlLexer.java")
    purgeOldFiles.set(true)
  }

  val generateHtlLexer by register<GenerateLexerTask>("generateHtlLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/_ClientlibDeclarationLexer.flex"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToClass.set("/com/aemtools/lang/clientlib/_ClientlibDeclarationLexer.java")
    purgeOldFiles.set(true)

    mustRunAfter(generateCdLexer)
  }

  val generateJpLexer by register<GenerateLexerTask>("generateJpLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/JcrPropertyLexer.flex"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToClass.set("/com/aemtools/lang/jcrproperty/_JcrPropertyLexer.java")
    purgeOldFiles.set(true)

    mustRunAfter(generateHtlLexer)
  }

  val generateElLexer by register<GenerateLexerTask>("generateElLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/el.flex"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToClass.set("/com/aemtools/lang/el/_ElLexer.java")
    purgeOldFiles.set(true)

    mustRunAfter(generateJpLexer)
  }

  val generateHtlPsiAndParser by register<GenerateParserTask>("generateHtlPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/Htl.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/htl/HtlParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/htl/psi")
    //purgeOldFiles.set(true)

    mustRunAfter(generateElLexer)
  }

  val generateCdPsiAndParser by register<GenerateParserTask>("generateCdPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/clientlibdeclaration.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/clientlib/ClientlibDeclarationParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/clientlib/psi")
    //purgeOldFiles.set(true)

    mustRunAfter(generateHtlPsiAndParser)
  }

  val generateJpPsiAndParser by register<GenerateParserTask>("generateJpPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/jcrproperty.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/jcrproperty/JcrPropertyParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/jcrproperty/psi")
    //purgeOldFiles.set(true)

    mustRunAfter(generateCdPsiAndParser)
  }

  val generateElPsiAndParser by register<GenerateParserTask>("generateElPsiAndParser") {
    group = "grammar"
    sourceFile.set(file("src/main/bnf/el.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("/com/aemtools/lang/el/ElParser.java")
    pathToPsiRoot.set("/com/aemtools/lang/el/psi")
    //purgeOldFiles.set(true)

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
