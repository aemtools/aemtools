package com.aemtools.completion.htl.dataslycall

import com.aemtools.common.constant.const
import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for [com.aemtools.completion.htl.provider.option.HtlOptionCompletionProvider].
 *
 * @author Dmytro Primshyts
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
