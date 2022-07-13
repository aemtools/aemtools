package com.aemtools.completion.html

import com.aemtools.common.constant.const.htl.DATA_SLY_SET
import com.aemtools.common.constant.const.htl.HTL_ATTRIBUTES
import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for HTL attributes completion for HTL v 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class HtlAttributesContributorV13Test : HtlAttributesContributorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun testHtlAttributes() = assertVariantsPresent(HTL_ATTRIBUTES - DATA_SLY_SET)
}
