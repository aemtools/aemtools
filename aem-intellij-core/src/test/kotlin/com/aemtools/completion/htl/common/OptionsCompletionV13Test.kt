package com.aemtools.completion.htl.common

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.BaseVariantsCheckContributorTest.Companion.CONTEXT_PARAMETERS

/**
 * Tests for HTL el options completion for HTL v 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class OptionsCompletionV13Test : OptionsCompletionTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun testOptionsWithinDataSlyResourceShouldHaveResourceTypeAsFirstOption() = completionTest {
    addHtml("test.html", """
            <div data-sly-resource='$DOLLAR{@ $CARET}'></div>
        """)
    shouldContain(listOf("resourceType") + CONTEXT_PARAMETERS)
  }

  fun testEmptyDataSlyListOptions() = completionTest {
    addHtml("test.html", """
            <div data-sly-list='$DOLLAR{@ $CARET}'></div>
        """)
    shouldNotContain(listOf("begin", "step", "end"))
  }

  fun testEmptyDataSlyRepeatOptions() = completionTest {
    addHtml("test.html", """
            <div data-sly-list='$DOLLAR{@ $CARET}'></div>
        """)
    shouldNotContain(listOf("begin", "step", "end"))
  }
}
