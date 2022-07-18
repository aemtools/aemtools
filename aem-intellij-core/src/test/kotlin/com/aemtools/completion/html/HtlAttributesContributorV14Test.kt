package com.aemtools.completion.html

import com.aemtools.common.constant.const
import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for HTL attributes completion for HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlAttributesContributorV14Test : HtlAttributesContributorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testHtlAttributes() = assertVariantsPresent(const.htl.HTL_ATTRIBUTES)
}
