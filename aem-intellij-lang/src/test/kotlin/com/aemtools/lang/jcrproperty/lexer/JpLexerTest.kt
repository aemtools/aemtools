package com.aemtools.lang.jcrproperty.lexer

import com.aemtools.test.lexer.LexerBaseTest
import com.intellij.lexer.Lexer

/**
 * @author Dmytro Primshyts
 */
class JpLexerTest : LexerBaseTest() {

  override fun getTestDataPath(): String = "com/aemtools/lang/jcrproperty/lexer/fixtures"

  override fun createLexer(): Lexer = JpLexer()
  override fun getDirPath(): String = "com.aemtools.lang.jcrproperty.lexer"

  override fun getInExtension(): String = "in"
  override fun getOutExtension(): String = "out"

  fun testValue() = doTest()
  fun testValueWithEscapedXmlAttrValueSpecialSymbols() = doTest()
  fun testValueWithEscapedXmlAttrValueSpecialSymbols2() = doTest()
  fun testValueWithEntityRefEscape() = doTest()
  fun testValueWithCharRefEscape() = doTest()
  fun testValueWithOtherStringEscapedSymbols() = doTest()
  fun testArray() = doTest()
  fun testEmptyArray() = doTest()
  fun testValueWithBracesAndBrackets() = doTest()
  fun testArrayWithRightBracketInToken() = doTest()
  fun testArrayWithLeadingRightBracketInToken() = doTest()
  fun testArrayWithEscapedComma() = doTest()
  fun testArrayWithEscapedXmlAttrValueSpecialSymbols() = doTest()
  fun testArrayWithEscapedXmlAttrValueSpecialSymbols2() = doTest()
  fun testArrayWithEntityRefEscape() = doTest()
  fun testArrayWithCharRefEscape() = doTest()
  fun testArrayWithOtherStringEscapedSymbols() = doTest()
}
