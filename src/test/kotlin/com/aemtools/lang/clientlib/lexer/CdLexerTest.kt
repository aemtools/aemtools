package com.aemtools.lang.clientlib.lexer

import com.aemtools.lang.htl.lexer.LexerBaseTest
import com.intellij.lexer.Lexer

/**
 * @author Dmytro_Troynikov
 */
class CdLexerTest : LexerBaseTest() {
  override fun getTestDataPath(): String
      = "com/aemtools/lang/clientlib/lexer/fixtures"

  override fun createLexer(): Lexer = CdLexer()
  override fun getDirPath(): String = "com.aemtools.lang.clientlib.lexer"
  override fun getInExtension(): String = "in"
  override fun getOutExtension(): String = "out"

  fun testSingleInclude() = doTest()
  fun testSingleIncludeWithBase() = doTest()
  fun testBaseCommentInclude() = doTest()

  fun testRelativeInclude1() = doTest()
  fun testRelativeInclude2() = doTest()
  fun testRelativeInclude3() = doTest()
}
