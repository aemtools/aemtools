package com.aemtools.completion.html

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * @author Dmytro Primshyts
 */
class HtmlLinkCheckerCompletionTest : CompletionBaseLightTest(false) {

  fun testAAttributesCompletion() = completionTest {
    addHtml("test.html", """
       <a href="" $CARET></a>
    """)

    shouldContain(listOf("x-cq-linkchecker"), false)
  }

  fun testLinkcheckerValuesCompletion() = completionTest {
    addHtml("test.html", """
        <a x-cq-linkchecker="$CARET"></a>
    """)

    shouldContain("skip", "valid")
  }
}
