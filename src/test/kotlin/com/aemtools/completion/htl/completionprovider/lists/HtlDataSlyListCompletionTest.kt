package com.aemtools.completion.htl.completionprovider.lists

import com.aemtools.completion.blocks.BaseVariantsCheckContributorTest

/**
 * @author Dmytro_Troynikov
 */
class HtlDataSlyListCompletionTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter/data-sly-list") {

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

}