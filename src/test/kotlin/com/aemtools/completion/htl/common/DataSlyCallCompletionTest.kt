package com.aemtools.completion.htl.common

import com.aemtools.blocks.completion.CompletionBaseLightTest

/**
 * @author Dmytro Troynikov
 */
class DataSlyCallCompletionTest : CompletionBaseLightTest() {

    fun testMain() = completionTest {
        addHtml("test.html", """
            <div data-sly-use.template="template.html">
                $DOLLAR{template.$CARET}
            </div>
            """.trimIndent()
        )
        addHtml("template.html", """
            <div data-sly-template.first="$DOLLAR{@ param1, param2}">

            </div>
            <div data-sly-template.second="$DOLLAR{@param1, param2}">
            </div>
        """.trimIndent())

        shouldContain(listOf("first", "second"))
    }



}