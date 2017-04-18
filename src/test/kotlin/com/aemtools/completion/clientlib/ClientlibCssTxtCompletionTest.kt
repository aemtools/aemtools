package com.aemtools.completion.clientlib

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro_Troynikov
 */
class ClientlibCssTxtCompletionTest : CompletionBaseLightTest() {

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

}