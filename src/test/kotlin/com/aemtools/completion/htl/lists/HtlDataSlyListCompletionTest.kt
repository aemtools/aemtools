package com.aemtools.completion.htl.lists

import com.aemtools.blocks.BaseVariantsWithJdkContributorTest
import com.aemtools.completion.htl.common.CollectionsTest.Companion.MY_MODEL_VARIANTS

/**
 * @author Dmytro_Troynikov
 */
class HtlDataSlyListCompletionTest : BaseVariantsWithJdkContributorTest("com/aemtools/completion/htl/fixtures/noafter/data-sly-list") {

  fun testDataSlyListDefaultVariables() =
      assertVariantsPresent(DEFAULT_LIST_VARIABLES)

  fun testDataSlyListOverridenVariables() =
      assertVariantsPresent(OVERRIDEN_LIST_VARIABLES)

  fun testDataSlyListVariablesNotAvailableInOuterScope() =
      assertVariantsAbsent(DEFAULT_LIST_VARIABLES)

  fun testDataSlyListOverridenVariablesNotAvailableInOuterScope() =
      assertVariantsAbsent(OVERRIDEN_LIST_VARIABLES)

  fun testDataSlyListListVariableFields() =
      assertVariants(LIST_VARIABLE_FIELDS)

  fun testDataSlyListOverridenListVariableFields() =
      assertVariants(LIST_VARIABLE_FIELDS)

  fun testDataSlyListResolveItem() =
      assertVariants(PAGE_FIELDS)

  fun testDataSlyListResolveOverridenItem() =
      assertVariants(PAGE_FIELDS)

  fun testDataSlyListResolveItemRecursive() =
      assertVariants(PAGE_FIELDS)

  fun testDataSlyListTwoLists() =
      assertVariants(MY_MODEL_VARIANTS)

  fun testDataSlyListTwoListsNested() =
      assertVariants(MY_MODEL_VARIANTS)

}
