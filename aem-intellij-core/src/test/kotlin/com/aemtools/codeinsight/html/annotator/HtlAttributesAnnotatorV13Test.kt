package com.aemtools.codeinsight.html.annotator

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Test for [HtlAttributesAnnotator] in HTL v 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class HtlAttributesAnnotatorV13Test: HtlAttributesAnnotatorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun testUnusedDataSlySet() {
    myFixture.configureByText("test.html", """
      <div <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">data-sly-set</error>.test=""></div>
    """)
    myFixture.testHighlighting()
  }

  fun testUnusedDataSlyUnwrap() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap.<error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">test</error>=<weak_warning descr="This expression has no effect in current HTL version 1.3. Support for this feature starts with HTL version 1.4.">""</weak_warning>>
      </div>
    """.trimIndent())
    myFixture.testHighlighting()
  }
}
