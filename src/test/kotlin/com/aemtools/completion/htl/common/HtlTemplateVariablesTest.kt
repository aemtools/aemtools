package com.aemtools.completion.htl.common

import com.aemtools.blocks.BaseVariantsCheckContributorTest

/**
 * @author Dmytro Troynikov
 */
class HtlTemplateVariablesTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter/template") {

  fun testTemplateVariablesResolution() = assertVariantsPresent(listOf("param1", "param2"))
  fun testTemplateVariablesOutOfTemplate() = assertVariantsAbsent(listOf("param1", "param2"))

  fun testTemplateOptionsDontHaveVariants() = assertVariantsAreEmpty()

}
