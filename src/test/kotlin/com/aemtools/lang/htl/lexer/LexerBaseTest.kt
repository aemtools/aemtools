package com.aemtools.lang.htl.lexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.testFramework.LexerTestCase
import com.intellij.testFramework.UsefulTestCase
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Dmytro_Troynikov
 */
abstract class LexerBaseTest : LexerTestCase(), HtlTestCase {
  override fun createLexer(): Lexer = com.aemtools.lang.htl.lexer.HtlLexer()

  open fun getInExtension(): String = "html"
  open fun getOutExtension(): String = "txt"

  protected fun doTest(lexer: Lexer = createLexer()) {
    val filePath = pathToSourceTestFile(getTestName(true), getInExtension())

    var text = ""
    try {
      val fileText = FileUtil.loadFile(filePath.toFile(), Charsets.UTF_8)
      text = StringUtil.convertLineSeparators(if (shouldTrim()) fileText.trim() else fileText)
    } catch (e: IOException) {
      fail("Unable to load file $filePath: ${e.message}")
    }
    doTest(text, null, lexer)
  }

  override fun doTest(text: String, expected: String?, lexer: Lexer) {
    val result = printTokens(text, 0, lexer)
    if (expected != null) {
      UsefulTestCase.assertSameLines(expected, result)
    } else {
      UsefulTestCase.assertSameLinesWithFile(pathToGoldTestFile(getTestName(true), getOutExtension()).toFile().canonicalPath, result)
    }
  }
}

interface HtlTestCase {
  fun getTestDataPath(): String

  companion object {
    val testResourcesPath = "src/test/resources"
  }
}

fun HtlTestCase.pathToSourceTestFile(name: String, extension: String = "html"): Path =
    Paths.get("${HtlTestCase.testResourcesPath}/${getTestDataPath()}/$name.$extension")

fun HtlTestCase.pathToGoldTestFile(name: String, extension: String = "txt"): Path =
    Paths.get("${HtlTestCase.testResourcesPath}/${getTestDataPath()}/$name.$extension")
