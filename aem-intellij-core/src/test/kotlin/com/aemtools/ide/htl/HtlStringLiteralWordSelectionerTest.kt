package com.aemtools.ide.htl

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BaseLightTest
import com.intellij.testFramework.fixtures.CodeInsightTestUtil

/**
 * @author Dmytro Primshyts
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
