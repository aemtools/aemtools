package com.aemtools.lang.htl.parser

/**
 * @author Dmytro Troynikov
 */
class HtlParserLiteralsTest
: HtlParserBaseTest("com/aemtools/lang/htl/parser/fixtures/literal") {

    fun testBooleanLiteralFalse() = doTest(true)
    fun testBooleanLiteralTrue() = doTest(true)
    fun testInteger() = doTest()

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

    override fun setUp() {
        super.setUp()
    }

}