import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

plugins {
  java
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatformModule)
  alias(libs.plugins.kover)
  alias(libs.plugins.grammarkit)
}

dependencies {
  implementation(project(":aem-intellij-common"))

  testImplementation(project(":test-framework"))

  implementation(libs.kotlinStdLib)
  implementation(libs.kotlinReflect)
  implementation(libs.kotlinStdLibJdk8)

  testImplementation(libs.assertjCore)
  testImplementation(libs.mockitoCore)
  testImplementation(libs.mockitoKotlin)

  // Use junit-bom to align versions
  // https://docs.gradle.org/current/userguide/managing_transitive_dependencies.html#sec:bom_import
  implementation(platform(libs.junitBom))  {
    because("Platform, Jupiter, and Vintage versions should match")
  }

  // JUnit Jupiter
  testImplementation(libs.junitJupiter)

  // JUnit Vintage
  testImplementation(libs.junit4)
  testRuntimeOnly(libs.junitVintageEngine) {
    because("allows JUnit 3 and JUnit 4 tests to run")
  }

  // JUnit Suites
  testImplementation(libs.junitPlatformSuite)

  // JUnit Platform Launcher + Console
  testRuntimeOnly(libs.junitPlatformLauncher) {
    because("allows tests to run from IDEs that bundle older version of launcher")
  }
  testRuntimeOnly(libs.junitPlatformConsole) {
    because("needed to launch the JUnit Platform Console program")
  }

  testImplementation(libs.spekApi) {
    exclude(group = "org.jetbrains.kotlin")
  }
  testRuntimeOnly(libs.spekJunitPlatformEngine) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
  }
  testImplementation(libs.spekSubjectExtension) {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.junit.platform")
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
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/htl/lexer"))
    purgeOldFiles.set(true)
  }

  val generateHtlLexer by register<GenerateLexerTask>("generateHtlLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/_ClientlibDeclarationLexer.flex"))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/clientlib"))
    //purgeOldFiles.set(true)

    mustRunAfter(generateCdLexer)
  }

  val generateJpLexer by register<GenerateLexerTask>("generateJpLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/JcrPropertyLexer.flex"))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/jcrproperty"))
    //purgeOldFiles.set(true)

    mustRunAfter(generateHtlLexer)
  }

  val generateElLexer by register<GenerateLexerTask>("generateElLexer") {
    group = "grammar"
    sourceFile.set(file("src/main/flex/el.flex"))
    targetOutputDir.set(file("src/main/gen/com/aemtools/lang/el"))
    //purgeOldFiles.set(true)

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
