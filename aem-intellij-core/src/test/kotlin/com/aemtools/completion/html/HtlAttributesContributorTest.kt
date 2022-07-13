package com.aemtools.completion.html

import com.aemtools.common.constant.const.htl.DATA_SLY_LIST
import com.aemtools.common.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.common.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.test.BaseVariantsCheckContributorTest

/**
 * @author Dmytro Primshyts
 */
abstract class HtlAttributesContributorTest
  : BaseVariantsCheckContributorTest("com/aemtools/completion/html/attributes") {

  fun testHtlAttributesFilterOutUnwrap() = assertVariantsAbsent(listOf(DATA_SLY_UNWRAP))

  fun testHtlAttributesFilterOutUnwrap2() = assertVariantsAbsent(listOf(DATA_SLY_UNWRAP))

  fun testHtlAttributesFilterOutDataSlyList() = assertVariantsAbsent(listOf(DATA_SLY_LIST))

  fun testHtlAttributesFilterOutDataSlyRepeat() = assertVariantsAbsent(listOf(DATA_SLY_REPEAT))

}
