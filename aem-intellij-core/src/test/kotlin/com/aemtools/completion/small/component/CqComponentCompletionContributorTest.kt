package com.aemtools.completion.small.component

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for [CqComponentCompletionContributor].
 *
 * @author Kostiantyn Diachenko
 */
class CqComponentCompletionContributorTest : CompletionBaseLightTest(false) {
  fun testFullCompletion() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" $CARET />
    """)
    shouldContain(
        "jcr:title",
        "jcr:description",
        "componentGroup",
        "sling:resourceSuperType",
        "cq:noDecoration",
        "cq:isContainer",
        "dialogPath",
        "cq:templatePath"
    )
  }

  fun testCompletionShouldFilterOutAlreadyPresentVariants() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:Component" jcr:title="Component title" $CARET />
    """)

    shouldContain(
        "jcr:description",
        "componentGroup",
        "sling:resourceSuperType",
        "cq:noDecoration",
        "cq:isContainer",
        "dialogPath",
        "cq:templatePath"
    )
    shouldNotContain("jcr:title")
  }

}
