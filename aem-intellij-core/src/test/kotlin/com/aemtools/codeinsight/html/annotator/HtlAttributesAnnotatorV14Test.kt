package com.aemtools.codeinsight.html.annotator

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Test for [HtlAttributesAnnotator] in HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlAttributesAnnotatorV14Test: HtlAttributesAnnotatorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testUnusedDataSlySet() {
    myFixture.configureByText("test.html", """
      <div data-sly-set.<warning descr="null">test</warning>=""></div>
    """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyUnwrap() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap.<warning descr="null">test</warning>=""></div>
    """)
    myFixture.testHighlighting()
  }
}
