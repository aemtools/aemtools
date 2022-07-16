package com.aemtools.lang.htl.parser

/**
 * @author Dmytro Primshyts
 */
class HtlParserLogicalOperations
  : HtlParserBaseTest("com/aemtools/lang/htl/parser/fixtures/logical") {

  fun testGreaterThan() = doTest()
  fun testLessThan() = doTest()
  fun testGreaterThanEquals() = doTest()
  fun testLessThanEquals() = doTest()
  fun testEquals() = doTest()
  fun testNotEquals() = doTest()

  fun testNestedBraces() = doTest()
  fun testBracedLogicalExpression() = doTest()

  fun testRelationalOperatorWithStringLiteral() = doTest()
  fun testRelationalOperatorWithProperty() = doTest()
  fun testRelationalOperatorWithArray() = doTest()
  fun testRelationalOperatorWithNumber() = doTest()
  fun testRelationalOperatorWithExpressionAndArray() = doTest()
  fun testRelationalOperatorWithTwoArrays() = doTest()
  fun testRelationalOperatorWithTwoProperties() = doTest()
}
