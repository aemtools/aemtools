package com.aemtools.completion.html.dataslyinclude

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * @author Dmytro Primshyts
 */
class DataSlyIncludeElCompletionTest : CompletionBaseLightTest() {

  fun testDataSlyInclude() = completionTest {
    addHtml("$JCR_ROOT/apps/myapp/component/component.html", """
            <div data-sly-include="$CARET"></div>
        """)
    addHtml("$JCR_ROOT/apps/myapp/component/htl-file.html", "")
    addHtml("$JCR_ROOT/apps/myapp/component/inner/inner-htl-file.html", "")
    addFile("$JCR_ROOT/apps/myapp/component/jsp-file.jsp", "")
    addFile("$JCR_ROOT/apps/myapp/component/inner/inner-jsp-file.jsp", "")
    shouldContain(
        "htl-file.html",
        "jsp-file.jsp",
        "inner/inner-htl-file.html",
        "inner/inner-jsp-file.jsp"
    )
  }

}
