package com.aemtools.completion.clientlib

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * @author Dmytro Primshyts
 */
class ClientlibCssTxtCompletionTest : CompletionBaseLightTest(false) {

  fun testBaseCompletionCss() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.less", "")
    shouldContain(listOf(
        "first.css",
        "css/second.less"
    ))
  }

  fun testCompletionWithBasePathCss() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.less", "")
    shouldContain(listOf(
        "first.css",
        "second.less"
    ))
  }

  fun testFilterOutDuplicates() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            first.css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.css", "")
    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun testFilterOutDuplicatesWithBasePath() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            css/first.css
            #base=css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.css", "")
    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun testFilterOutDuplicatesAfter() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
          $CARET
          first.css
    """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.css", "")

    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun testFilterOutDuplicatesWithBasePathAfter() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
          $CARET
          #base=css
          first.css
    """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.css", "")

    shouldContain(listOf("css/second.css"))
    shouldNotContain(listOf("css/first.css", "first.css"))
  }

  fun testBasePathCompletion1() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=$CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/file.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/file.css", "")
    shouldContain(listOf("css"))
    shouldNotContain(listOf("js"))
  }

  fun testBasePathCompletion2() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=$CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/file1.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/inner/file2.less", "")
    shouldContain(listOf(
        "css",
        "css/inner"
    ))
  }

}
