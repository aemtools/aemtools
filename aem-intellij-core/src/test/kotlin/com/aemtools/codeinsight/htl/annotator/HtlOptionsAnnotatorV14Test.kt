package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for [HtlOptionsAnnotator] for HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlOptionsAnnotatorV14Test : HtlOptionsAnnotatorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testDataSlyListOptions() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-list</info>="$DOLLAR{ @  <weak_warning descr="Iterable Parameter">begin</weak_warning>=''}"></div>
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testDataSlyRepeatOptions() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-repeat</info>="$DOLLAR{ @  <weak_warning descr="Iterable Parameter">begin</weak_warning>=''}"></div>
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testDataSlyResourceResourceTypeOption() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-resource</info>="$DOLLAR{ 'content' @  <weak_warning descr="Standard Option">resourceType</weak_warning>=''}"></div>
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testDataSlyResourceWcmModeOption() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-resource</info>="$DOLLAR{ 'content' @  <weak_warning descr="Standard Option">wcmmode</weak_warning>=''}"></div>
        """)
    myFixture.checkHighlighting(true, true, true)
  }
}
