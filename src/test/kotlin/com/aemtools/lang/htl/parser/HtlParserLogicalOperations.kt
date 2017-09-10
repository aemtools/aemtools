package com.aemtools.lang.htl.parser

import com.intellij.testFramework.ParsingTestCase

/**
 * @author Dmytro Troynikov
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
    fun testMultipleOperations() = doTest()
    fun testBracedLogicalExpression() = doTest()

}