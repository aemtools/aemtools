package com.aemtools.lang.htl.lexer

/**
 * @author Dmytro Troynikov.
 */
class HtlLexerTest : LexerBaseTest(), HtlTestCase {
  override fun getTestDataPath(): String
      = "com/aemtools/lang/htl/lexer/fixtures"

  fun testStringLiteral() = doTest()
  fun testDoubleQuotedStringLiteral() = doTest()
  fun testStringWithEscapeSymbols() = doTest()
  fun testEmptyString() = doTest()

  fun testNumberLiteral() = doTest()
  fun testBooleanLiteral() = doTest()
  fun testNullLiteral() = doTest()
  fun testLogicalAnd() = doTest()
  fun testLogicalOr() = doTest()
  fun testComparison() = doTest()
  fun testEquality() = doTest()
  fun testGroupedBooleanExpression() = doTest()

  fun testSimpleOption() = doTest()
  fun testMultipleOptions() = doTest()
  fun testOptionStandalone() = doTest()

  fun testTernaryOperator() = doTest()
  fun testTernaryOperatorWithLogicalOperator() = doTest()

  fun testVariableAccess() = doTest()
  fun testVariableAccessWithContext() = doTest()
  fun testNegate() = doTest()

  fun testElInAttribute() = doTest()
  fun testElInScript() = doTest()
  fun testElInStyle() = doTest()
  fun testElWithContext() = doTest()

  fun testComplexFile() = doTest()

  fun testUnclosedEl() = doTest()
  fun testEmptyEl() = doTest()

  override fun getDirPath(): String? = "com.aemtools.lang.htl.lexer"

}
