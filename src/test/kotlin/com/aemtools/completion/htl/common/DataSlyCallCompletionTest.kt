package com.aemtools.completion.htl.common

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

    fun testDataSlyCallParameters() = completionTest {
        addHtml("test.html", """
            <div data-sly-template.template="$DOLLAR{@ param1, param2}"></div>

            <div data-sly-call="$DOLLAR{template @ $CARET }"></div>
        """)
        shouldContain(listOf("param1", "param2"))
    }

    fun testDataSlyCallParametersFromAnotherFile() = completionTest {
        addHtml("$JCR_ROOT/apps/components/comp/comp.html", """
            <div data-sly-use.template="template.html">
                <div data-sly-call="$DOLLAR{template.template @ $CARET}"></div>
            </div>
        """)
        addHtml("$JCR_ROOT/apps/components/comp/template.html", """
            <div data-sly-template.template="$DOLLAR{@ param1, param2}"></div>
        """)
        shouldContain(listOf("param1", "param2"))
    }

}