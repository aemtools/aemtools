package com.aemtools.ide.refactoring

import com.aemtools.blocks.base.BaseLightTest

/**
 * @author Dmytro Troynikov
 */
class TemplateParametersRenameTest : BaseLightTest() {

    fun testOptionRename() = fileCase {
        addHtml("test.html", """
            <div data-sly-template.template="$DOLLAR{@ ${CARET}param}">
                $DOLLAR{param @ context=param}
            </div>
        """.trimIndent())

        verify {
            myFixture.renameElementAtCaret("renamed")

            myFixture.checkResult("""
                <div data-sly-template.template="$DOLLAR{@ ${CARET}renamed}">
                    $DOLLAR{renamed @ context=renamed}
                </div>
            """.trimIndent())
        }
    }

    fun testUsedOptionRename() = fileCase {
        addHtml("test.html", """
            <div data-sly-template.template="$DOLLAR{ @ param}">
                $DOLLAR{${CARET}param}
            <div>
        """.trimIndent())

        verify {
            myFixture.renameElementAtCaret("renamed")

            myFixture.checkResult("""
                <div data-sly-template.template="$DOLLAR{ @ renamed}">
                    $DOLLAR{${CARET}renamed}
                </div>
            """.trimIndent())
        }
    }

}