package com.aemtools.lang.reppolicy.psi.pattern

import com.aemtools.blocks.pattern.BasePatternsTest

/**
 * @author Dmytro Troynikov
 */
class RepPolicyPatternsTest : BasePatternsTest() {

    fun testRepPolicyAttribute() = testPattern(
            RepPolicyPatterns.attributeName,
        "<tag $CARET />",
            true,
            fixtureSetup = { textToAdd: String ->
                addXml("_rep_policy.xml", textToAdd)
            }
    )

}