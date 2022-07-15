package com.aemtools.lang.htl.parser

/**
 * @author Dmytro Primshyts
 */
class HtlParserGeneralTest
  : HtlParserBaseTest("com/aemtools/lang/htl/parser/fixtures/general") {

  fun testPropertyAccess() = doTest()
  fun testPropertyAccessWithContext() = doTest()
  fun testPropertyAccessArray() = doTest()
  fun testPropertyAccessArrayDoublequoted() = doTest()
  fun testPropertyAccessArrayNumber() = doTest()
  fun testPropertyAccessWithVariable() = doTest()

  fun testContext() = doTest()
  fun testContextNoValue() = doTest()
  fun testContextAndExpression() = doTest()
  fun testContextAndComplexExpression() = doTest()
  fun testContextWithMultipleAssignments() = doTest()
  fun testContextWithBothAssignmentAndNonAssignment() = doTest()
  fun testContextAndRelationalExpression() = doTest()
  fun testContextAndRelationalExpressionInParenthesis() = doTest()
  fun testContextAndComplexExpressionInParenthesis() = doTest()
  fun testContextAndComplexExpressionInParenthesis2() = doTest()

  fun testMultipleEl() = doTest()

  fun testUnclosedEl() = doTest()
  fun testEmptyEl() = doTest()
  fun testDataSlyUse() = doTest()

  fun testGroupingParenthesis() = doTest()
  fun testGroupingParenthesisInArray() = doTest()

  fun testCorruptedEl() = doTest()

  fun testContextWithTernaryOperator() = doTest()

  fun testTernaryOperator() = doTest()
  fun testTernaryOperatorWithLogicalOperator() = doTest()
  fun testTernaryOperatorWithThreeElements() = doTest()
  fun testTernaryOperatorWithRelationalOperator() = doTest()
  fun testTernaryOperatorWithRelationalOperatorAfterColon() = doTest()
  fun testTernaryOperatorWithRelationalOperatorAfterQuestion() = doTest()
  fun testTernaryOperatorInArrayWithRelationalOperator() = doTest()
  fun testTernaryOperatorInArrayWithRelationalOperatorAfterColon() = doTest()
  fun testTernaryOperatorInArrayWithRelationalOperatorAfterQuestion() = doTest()

  fun testComment() = doTest()
  fun testCommentMultiline() = doTest()
}
