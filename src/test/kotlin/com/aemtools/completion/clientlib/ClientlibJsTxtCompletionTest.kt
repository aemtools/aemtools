package com.aemtools.completion.clientlib

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro_Troynikov
 */
class ClientlibJsTxtCompletionTest : CompletionBaseLightTest() {
    fun testBaseCompletionJs() = completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            $CARET
        """)
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.js", "")
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/second.js", "")
        shouldContain(listOf(
                "first.js",
                "js/second.js"
        ))
    }

    fun testCompletionWithBasePathJs() = completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=js
            $CARET
        """)
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/first.js", "")
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/second.js", "")
        shouldContain(listOf(
                "first.js",
                "second.js"
        ))
    }

    fun testFilterOutDuplicates() = completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            first.js
            $CARET
        """)
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.js", "")
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.js", "")
        shouldContain(listOf("second.js"))
        shouldNotContain(listOf("first.js"))
    }

    fun testBasePathCompletion() = completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=$CARET
        """)
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/file.js", "")
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/file.css", "")
        shouldContain(listOf("js"))
        shouldNotContain(listOf("css"))
    }

    fun testBasePathCompletion2() = completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=$CARET
        """)
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/first.js", "")
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/inner/second.js", "")
        shouldContain(listOf("js", "js/inner"))
    }

}