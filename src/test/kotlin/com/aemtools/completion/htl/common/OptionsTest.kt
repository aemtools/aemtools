package com.aemtools.completion.htl.common

import com.aemtools.blocks.BaseVariantsCheckContributorTest

/**
 * @author Dmytro_Troynikov
 */
class OptionsTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter/options") {

    fun testOptionsContextValues() = assertVariants(CONTEXT_VALUES)
    fun testOptionsContextValuesInAttribute() = assertVariants(CONTEXT_VALUES)
    fun testOptionsContextValuesAbsentWithoutString() = assertVariantsAbsent(CONTEXT_VALUES)

    fun testOptionsDefaultContextParameters() = assertVariants(CONTEXT_PARAMETERS)

    fun testOptionsCompletionWithCorruptedEl() = assertVariants(CONTEXT_PARAMETERS)

    fun testOptionsDefaultContextParametersFiltering()
            = assertVariants(CONTEXT_PARAMETERS - listOf("i18n", "join"))
    fun testOptionsContextObjectsShouldBeProposedAsCompletionVariants() = assertVariants(DEFAULT_CONTEXT_OBJECTS)

}