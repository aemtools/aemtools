package com.aemtools.ide.htl

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.lang.htl.lexer.HtlTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestUtil

/**
 * @author Dmytro Troynikov
 */
class HtlStringLiteralWordSelectionerTest : BaseLightTest(false) {

  fun testDoublequotedLiteral() = doTest()

  fun testSinglequotedLiteral() = doTest()

  override fun getTestDataPath(): String = HtlTestCase.testResourcesPath

  private fun doTest() {
    CodeInsightTestUtil.doWordSelectionTestOnDirectory(
        myFixture,
        "/com/aemtools/ide/selectword/${getTestName(true)}",
        "html"
    )
  }

}
