package com.aemtools.completion.htl.common

import com.aemtools.blocks.BaseVariantsCheckContributorTest
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Dmytro Troynikov
 */
class PredefinedVariablesTest : BaseVariantsCheckContributorTest("com/aemtools/completion/htl/fixtures/noafter") {

    fun testContextObjectFieldsResolution() {
        myFixture.configureByFile(fileName)
        val variants = myFixture.completeBasic()
        assertThat(variants).isNotNull()
    }

}