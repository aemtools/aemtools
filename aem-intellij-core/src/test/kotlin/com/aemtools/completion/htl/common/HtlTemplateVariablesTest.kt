package com.aemtools.completion.htl.common

import com.aemtools.test.BaseVariantsCheckContributorTest

/**
 * @author Dmytro Primshyts
 */
class HtlTemplateVariablesTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter/template") {

  fun testTemplateVariablesResolution() = assertVariantsPresent(listOf("param1", "param2"))
  fun testTemplateVariablesOutOfTemplate() = assertVariantsAbsent(listOf("param1", "param2"))

  fun testTemplateOptionsDontHaveVariants() = assertVariantsAreEmpty()

}
