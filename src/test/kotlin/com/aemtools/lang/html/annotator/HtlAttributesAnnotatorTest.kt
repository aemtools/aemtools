package com.aemtools.lang.html.annotator

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

/**
 * @author Dmytro Troynikov
 */
class HtlAttributesAnnotatorTest : LightCodeInsightFixtureTestCase() {

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
