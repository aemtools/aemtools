package com.aemtools.lang.jcrproperty.lexer

import com.aemtools.test.lexer.LexerBaseTest
import com.intellij.lexer.Lexer

/**
 * @author Dmytro Primshyts
 */
class JpLexerTest : LexerBaseTest() {

  override fun getTestDataPath(): String
  = "com/aemtools/lang/jcrproperty/lexer/fixtures"

  override fun createLexer(): Lexer = JpLexer()
  override fun getDirPath(): String = "com.aemtools.lang.jcrproperty.lexer"

  override fun getInExtension(): String = "in"
  override fun getOutExtension(): String = "out"

  fun testValue() = doTest()
  fun testArray() = doTest()
  fun testEmptyArray() = doTest()
}
