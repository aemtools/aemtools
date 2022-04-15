package com.aemtools.lang.clientlib.parser

import com.aemtools.lang.clientlib.CdParserDefinition
import com.aemtools.test.parser.BaseParserTest

/**
 * @author Dmytro Primshyts
 */
class CdParserTest
  : BaseParserTest(
    "com/aemtools/lang/clientlib/parser/fixtures",
    "in",
    CdParserDefinition()
) {

  fun testInclude() = doTest()
  fun testComplex1() = doTest()
  fun testCommentBaseInclude() = doTest()
  fun testBaseEmpty() = doTest()
  fun testBaseInclude() = doTest()
  fun testBaseCommentInclude() = doTest()

  fun testRelativeInclude1() = doTest()
  fun testRelativeInclude2() = doTest()
  fun testRelativeInclude3() = doTest()

}
