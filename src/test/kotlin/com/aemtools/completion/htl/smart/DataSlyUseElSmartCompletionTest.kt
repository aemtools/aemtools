package com.aemtools.completion.htl.smart

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro Troynikov
 */
class DataSlyUseElSmartCompletionTest : CompletionBaseLightTest() {

  fun testSmartCompletion() = completionTest {
    addHtml("$JCR_ROOT/apps/component/component.html", """
            <div data-sly-use="$DOLLAR{'$CARET'}"></div>
        """)
    addHtml("$JCR_ROOT/apps/component/partials/piece1.html", """
            <div data-sly-template.template="$DOLLAR{@ param}"></div>
        """)
    addHtml("$JCR_ROOT/apps/component/piece2.html", """
            <div data-sly-template.template="$DOLLAR{@ param}"></div>
        """)
    addClass("ComponentModel.java", """
            package com.test;

            import com.adobe.cq.sightly.WCMUse;

            public class ComponentUse extends WCMUse {}
        """)
    smart()
    shouldContain(listOf(
        "partials/piece1.html",
        "piece2.html",
        "com.test.ComponentUse"
    ))
  }

}

