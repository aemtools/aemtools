package com.aemtools.lang.jcrproperty.lexer

import com.aemtools.test.lexer.LexerBaseTest
import com.intellij.lexer.Lexer

/**
 * @author Dmytro Primshyts
 */
class JpLexerTypesTest : LexerBaseTest() {

  override fun getTestDataPath(): String = "com/aemtools/lang/jcrproperty/lexer/fixtures/types"

  override fun createLexer(): Lexer = JpLexer()
  override fun getDirPath(): String = "com.aemtools.lang.jcrproperty.lexer"

  override fun getInExtension(): String = "in"
  override fun getOutExtension(): String = "out"

  fun testBinary() = doTest()
  fun testBoolean() = doTest()
  fun testDate() = doTest()
  fun testDecimal() = doTest()
  fun testDouble() = doTest()
  fun testLong() = doTest()
  fun testName() = doTest()
  fun testPath() = doTest()
  fun testReference() = doTest()
  fun testString() = doTest()
  fun testUri() = doTest()
  fun testWeakReference() = doTest()

}
