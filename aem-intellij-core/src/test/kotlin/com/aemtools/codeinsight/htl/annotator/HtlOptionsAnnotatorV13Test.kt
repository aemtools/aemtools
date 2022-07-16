package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for [HtlOptionsAnnotator] for HTL v 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class HtlOptionsAnnotatorV13Test : HtlOptionsAnnotatorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun testUnsupportedDataSlyListOptions() {
    myFixture.configureByText("test.html", """
      <div <info descr="null">data-sly-list</info>="$DOLLAR{ @  <weak_warning descr="This option has no effect in current HTL version 1.3. Support for this option starts with HTL version 1.4.">begin</weak_warning>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testUnsupportedDataSlyRepeatOptions() {
    myFixture.configureByText("test.html", """
      <div <info descr="null">data-sly-repeat</info>="$DOLLAR{ @  <weak_warning descr="This option has no effect in current HTL version 1.3. Support for this option starts with HTL version 1.4.">begin</weak_warning>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testDataSlyResourceResourceTypeOption() {
    myFixture.configureByText("test.html", """
      <div <info descr="null">data-sly-resource</info>="$DOLLAR{ 'content' @  <weak_warning descr="Standard Option">resourceType</weak_warning>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testUnsupportedDataSlyResourceWcmModeOption() {
    myFixture.configureByText("test.html", """
      <div <info descr="null">data-sly-resource</info>="$DOLLAR{ 'content' @  wcmmode=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}
