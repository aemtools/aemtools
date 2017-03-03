package com.aemtools.completion.htl.completionprovider

import com.aemtools.blocks.BaseVariantsCheckContributorTest

/**
 * Tests for Htl variables resolution.
 *
 * @author Dmytro Troynikov
 */
class HtlVariablesCommonTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter/htlvariables") {

    fun testDataSlyUseValueNoEl() = assertVariants(DATA_SLY_SUITABLE_CLASSES)
    fun testDataSlyUseValue() = assertVariants(DATA_SLY_SUITABLE_CLASSES)

    fun testDataSlyUseValueContextString() = assertVariantsAreEmpty()
    fun testStringLiteralShouldBeEmpty() = assertVariantsAreEmpty()

    fun testDataSlyUseSecondLevelVariableDeclaredInEl() =
            assertVariants(CUSTOM_MODEL_FIELDS)

    fun testDataSlyUseSecondLevelVariableDeclaredInElWithOption() =
            assertVariants(CUSTOM_MODEL_FIELDS)

}