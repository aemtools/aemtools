package com.aemtools.codeinsight.html.annotator

import com.aemtools.test.base.BaseLightTest

/**
 * Test for [HtlAttributesAnnotator].
 *
 * @author Dmytro Primshyts
 */
abstract class HtlAttributesAnnotatorTest : BaseLightTest() {

  fun testUnusedDataSlyUse() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.<warning descr="null">bean</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyTest() {
    myFixture.configureByText("test.html", """
            <div data-sly-test.<warning descr="null">test</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyList() {
    myFixture.configureByText("test.html", """
            <div data-sly-list.<warning descr="null">model</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyRepeat() {
    myFixture.configureByText("test.html", """
            <div data-sly-repeat.<warning descr="null">model</warning>=""></div>
        """)
    myFixture.testHighlighting()
  }

}
