package com.aemtools.lang.jcrproperty.parser

import com.aemtools.lang.jcrproperty.JcrPropertyParserDefinition
import com.aemtools.test.parser.BaseParserTest

/**
 * @author Dmytro Primshyts
 */
class JpParserTest :
    BaseParserTest(
        "com/aemtools/lang/jcrproperty/parser/fixtures/general",
        "in",
        JcrPropertyParserDefinition()
    ) {

  fun testValue() = doTest()
  fun testArray() = doTest()
  fun testEmptyArray() = doTest()
  fun testTypedArray() = doTest()

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
