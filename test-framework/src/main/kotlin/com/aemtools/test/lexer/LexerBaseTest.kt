package com.aemtools.test.lexer

import com.aemtools.lang.htl.lexer.HtlLexer
import com.aemtools.test.HtlTestCase
import com.aemtools.test.pathToGoldTestFile
import com.aemtools.test.pathToSourceTestFile
import com.intellij.lexer.Lexer
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.testFramework.LexerTestCase
import com.intellij.testFramework.UsefulTestCase
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Dmytro Primshyts
 */
abstract class LexerBaseTest : LexerTestCase(), HtlTestCase {
  override fun createLexer(): Lexer = HtlLexer()

  open fun getInExtension(): String = "html"
  open fun getOutExtension(): String = "txt"

  protected fun doTest(lexer: Lexer = createLexer()) {
    val filePath = pathToSourceTestFile(getTestName(true), getInExtension())

    var text = ""
    try {
      val absolutePath = Paths.get(filePath.toFile().absolutePath)

      val fileText = FileUtil.loadFile(absolutePath.toFile(), Charsets.UTF_8)
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
