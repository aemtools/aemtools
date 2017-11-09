package com.aemtools.lang.el.parser

import com.aemtools.lang.el.ElParserDefinition
import com.aemtools.test.parser.BaseParserTest

/**
 * @author Dmytro Troynikov
 */
class ElParserFunctionTest
  : BaseParserTest(
    "com/aemtools/lang/el/parser/fixtures/function",
    "el",
    ElParserDefinition()
) {

  fun testMemberAccess() = doTest()

}
