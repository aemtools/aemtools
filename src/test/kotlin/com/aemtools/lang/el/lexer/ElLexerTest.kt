package com.aemtools.lang.el.lexer

import com.aemtools.test.HtlTestCase
import com.aemtools.test.lexer.LexerBaseTest
import com.intellij.lexer.Lexer

/**
 * @author Dmytro Troynikov
 */
class ElLexerTest : LexerBaseTest(), HtlTestCase {
  override fun getDirPath(): String = "com.aemtools.lang.el.lexer"
  override fun createLexer(): Lexer = ElLexer()
  override fun getTestDataPath(): String = "com/aemtools/lang/el/lexer/fixtures"
  override fun getInExtension(): String = "el"

  fun testStringLiteral() = doTest()
  fun testStringLiteralDoublequoted() = doTest()

  fun testRegularExpression() = doTest()
  fun testDeferredExpression() = doTest()

  fun testVariable() = doTest()
  fun testFunction() = doTest()

}
