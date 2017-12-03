package com.aemtools.lang.htl.parser

/**
 * @author Dmytro Troynikov
 */
class HtlParserLiteralsTest
  : HtlParserBaseTest("com/aemtools/lang/htl/parser/fixtures/literal") {

  fun testBooleanLiteralFalse() = doTest()
  fun testBooleanLiteralTrue() = doTest()
  fun testInteger() = doTest()
  fun testNullLiteral() = doTest()

  fun testSingleQuotedString() = doTest()
  fun testDoubleQuotedString() = doTest()
  fun testEmptySingleQuotedString() = doTest()
  fun testTwoEmptyStrings() = doTest()

  fun testArrayWithLiterals() = doTest()
  fun testArrayWithVariables() = doTest()
  fun testArrayWithSingleElement() = doTest()
  fun testArrayWithExpression() = doTest()
  fun testArrayWithNestedArray() = doTest()
  fun testArrayWithTernaryOperation() = doTest()

}
