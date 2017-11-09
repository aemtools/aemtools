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

  fun testStringLiteralEscapedChars() = doTest()
  fun testStringLiteralEscapedChars2() = doTest()

  fun testRegularExpression() = doTest()
  fun testDeferredExpression() = doTest()

  fun testTrue() = doTest()
  fun testFalse()= doTest()

  fun testNull() = doTest()

  fun testNumberLiteral() = doTest()
  fun testfloatLiteral() = doTest()

  fun testVariable() = doTest()
  fun testFunction() = doTest()

  fun testAnd0() = doTest()
  fun testAnd1() = doTest()

  fun testOr0() = doTest()
  fun testOr1() = doTest()

  fun testEq0() = doTest()
  fun testEq1() = doTest()
  fun testNeq0() = doTest()
  fun testNeq1() = doTest()

}
