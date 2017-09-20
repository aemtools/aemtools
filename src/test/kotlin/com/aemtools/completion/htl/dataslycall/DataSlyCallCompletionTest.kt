package com.aemtools.completion.htl.dataslycall

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro Troynikov
 */
class DataSlyCallCompletionTest : CompletionBaseLightTest() {

  fun testDataSlyCallTemplateDeclaredInSameDirectory() = completionTest {
    addHtml("$JCR_ROOT/test.html", """
            <div data-sly-use.template="template.html">
                $DOLLAR{template.$CARET}
            </div>
            """)
    addHtml("$JCR_ROOT/template.html", """
            <div data-sly-template.first="$DOLLAR{@ param1, param2}">

            </div>
            <div data-sly-template.second="$DOLLAR{@param1, param2}">
            </div>
        """)

    shouldContain(listOf("first", "second"))
  }

  fun testDataSlyCallTemplateInSiblingDirectory() = completionTest {
    addHtml("$JCR_ROOT/apps/components/component1/component1.html", """
            <div data-sly-use.template="/apps/components/component2/component2.html">
                $DOLLAR{template.$CARET}
            </div>
        """)

    addHtml("$JCR_ROOT/apps/components/component2/component2.html", """
            <div data-sly-template.first="$DOLLAR{@ param1, param2}"></div>
            <div data-sly-template.second="$DOLLAR{@ param1, param2}"></div>
        """)
    shouldContain(listOf("first", "second"))
  }

  fun testDataSlyCallTemplatesFromSameFile() = completionTest {
    addHtml("test.html", """
            <div data-sly-template.template1="$DOLLAR{@ }"></div>
            <div data-sly-template.template2="$DOLLAR{@ }"></div>
            <div data-sly-call="$DOLLAR{$CARET}"></div>
        """)

    shouldContain(listOf("template1", "template2"))
  }

  fun testDataSlyCallTemplateShouldBeProposedForDataSlyCall() = completionTest {
    addHtml("test.html", """
            <div data-sly-template.template=''></div>
            <div data-sly-call='$DOLLAR{$CARET}'></div>
        """)
    shouldContain(listOf("template"))
  }

  fun testDataSlyCallTemplateShouldNotBeProposedOutsideOfSlyCall() = completionTest {
    addHtml("test.html", """
            <div data-sly-template.template=''></div>
            $DOLLAR{$CARET}
        """)
    shouldNotContain(listOf("template"))
  }

  fun testDataSlyCallComplex() = completionTest {
    addHtml("$JCR_ROOT/apps/components/comp/comp.html", """
            <div data-sly-use.temp1='template1.html'
                 data-sly-use.temp2='template2.html'
                 data-sly-use.inner='inner/template.html'
                 data-sly-use.outer='/apps/components/comp2/comp2.html'></div>
            <template data-sly-template.local=''></template>
            <div data-sly-call='$DOLLAR{$CARET}'></div>
        """)

    addHtml("$JCR_ROOT/apps/components/comp/template1.html", """
            <template data-sly-template.template1=''></template>
        """)
    addHtml("$JCR_ROOT/apps/components/comp/template2.html", """
            <template data-sly-template.template1=''></template>
            <template data-sly-template.template2=''></template>
        """)
    addHtml("$JCR_ROOT/apps/components/comp/inner/template.html", """
            <template data-sly-template.inner=''></template>
        """)
    addHtml("$JCR_ROOT/apps/components/comp2/comp2.html", """
            <template data-sly-template.comp2=''></template>
        """)

    shouldContain(listOf(
        "temp1.template1",
        "temp2.template1",
        "temp2.template2",
        "inner.inner",
        "outer.comp2",
        "local"
    ))
  }

}
