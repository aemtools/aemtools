package com.aemtools.completion.htl.dataslycall

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const

/**
 * @author Dmytro Troynikov
 */
class DataSlyCallOptionsCompletionTest : CompletionBaseLightTest() {

  fun testDataSlyCallParameters() = completionTest {
    addHtml("test.html", """
            <div data-sly-template.template="$DOLLAR{@ param1, param2}"></div>

            <div data-sly-call="$DOLLAR{template @ $CARET }"></div>
        """)
    shouldContain(listOf("param1", "param2"))
  }

  fun testDataSlyCallParametersFromAnotherFile() = completionTest {
    addHtml("${const.JCR_ROOT}/apps/components/comp/comp.html", """
            <div data-sly-use.template="template.html">
                <div data-sly-call="$DOLLAR{template.template @ $CARET}"></div>
            </div>
        """)
    addHtml("${const.JCR_ROOT}/apps/components/comp/template.html", """
            <div data-sly-template.template="$DOLLAR{@ param1, param2}"></div>
        """)
    shouldContain(listOf("param1", "param2"))
  }

  fun testDataSlyCallNoDuplicates() = completionTest {
    addHtml("test.html", """
            <div data-sly-template.template='$DOLLAR{@ param1, param2}'></div>
            <div data-sly-call='$DOLLAR{template @ param1, $CARET}'></div>
        """)
    shouldContain(listOf("param2"))
  }

}
