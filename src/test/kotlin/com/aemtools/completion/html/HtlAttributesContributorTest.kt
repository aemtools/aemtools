package com.aemtools.completion.html

import com.aemtools.blocks.BaseVariantsCheckContributorTest
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.constant.const.htl.HTL_ATTRIBUTES

/**
 * @author Dmytro_Troynikov
 */
class HtlAttributesContributorTest : BaseVariantsCheckContributorTest("com/aemtools/completion/html/attributes") {

  fun testHtlAttributes() = assertVariantsPresent(HTL_ATTRIBUTES)

  fun testHtlAttributesFilterOutUnwrap() = assertVariantsAbsent(listOf(DATA_SLY_UNWRAP))

  fun testHtlAttributesFilterOutUnwrap2() = assertVariantsAbsent(listOf(DATA_SLY_UNWRAP))

  fun testHtlAttributesFilterOutDataSlyList() = assertVariantsAbsent(listOf(DATA_SLY_LIST))

  fun testHtlAttributesFilterOutDataSlyRepeat() = assertVariantsAbsent(listOf(DATA_SLY_REPEAT))

}
