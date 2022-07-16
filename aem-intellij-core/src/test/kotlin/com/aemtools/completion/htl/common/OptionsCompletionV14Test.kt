package com.aemtools.completion.htl.common

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.BaseVariantsCheckContributorTest.Companion.CONTEXT_PARAMETERS

/**
 * Tests for HTL el options completion for HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class OptionsCompletionV14Test : OptionsCompletionTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testOptionsWithinDataSlyResourceShouldHaveResourceTypeAsFirstOption() = completionTest {
    addHtml("test.html", """
            <div data-sly-resource='$DOLLAR{@ $CARET}'></div>
        """)
    shouldContain(listOf("resourceType", "wcmmode", "decorationTagName", "cssClassName") + CONTEXT_PARAMETERS)
  }

  fun testDataSlyListOptions() = completionTest {
    addHtml("test.html", """
            <div data-sly-list='$DOLLAR{@ $CARET}'></div>
        """)
    shouldContain(listOf("begin", "step", "end"))
  }

  fun testDataSlyListOptionsFilterOutExisted() = completionTest {
    addHtml("test.html", """
            <div data-sly-list='$DOLLAR{@ step=1, $CARET}'></div>
        """)
    shouldContain(listOf("begin", "end"))
  }

  fun testDataSlyRepeatOptions() = completionTest {
    addHtml("test.html", """
            <div data-sly-list='$DOLLAR{@ $CARET}'></div>
        """)
    shouldContain(listOf("begin", "step", "end"))
  }

  fun testDataSlyRepeatOptionsFilterOutExisted() = completionTest {
    addHtml("test.html", """
            <div data-sly-list='$DOLLAR{@ begin=0, $CARET}'></div>
        """)
    shouldContain(listOf("step", "end"))
  }
}
