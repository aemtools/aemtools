package com.aemtools.completion.htl.completionprovider

import com.aemtools.completion.blocks.BaseVariantsCheckContributorTest

/**
 * Tests for Htl variables resolution.
 *
 * @author Dmytro Troynikov
 */
class HtlVariablesCommonTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter/htlvariables") {

    fun testDefaultContextParameters() = assertVariants(CONTEXT_PARAMETERS)

    fun testCompletionWithCorruptedEl() = assertVariants(CONTEXT_PARAMETERS)

    fun testDefaultContextParametersFiltering()
            = assertVariants(CONTEXT_PARAMETERS - listOf("i18n", "join"))

    fun testDataSlyUseValueNoEl() = assertVariants(DATA_SLY_SUITABLE_CLASSES)
    fun testDataSlyUseValue() = assertVariants(DATA_SLY_SUITABLE_CLASSES)

    fun testDataSlyUseValueContextString() = assertVariantsAreEmpty()
    fun testStringLiteralShouldBeEmpty() = assertVariantsAreEmpty()

    fun testDataSlyUseSecondLevelVariableDeclaredInEl() =
            assertVariants(CUSTOM_MODEL_FIELDS)

}