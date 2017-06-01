package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest

/**
 * @author Dmytro Troynikov
 */
class TemplateParametersRenameTest : BaseRenameTest() {

    fun testOptionRename() = renameCase {
        before {
            addHtml("test.html", """
                <div data-sly-template.template="$DOLLAR{@ ${CARET}param}">
                    $DOLLAR{param @ context=param}
                </div>
            """)
        }
        renameTo("renamed")
        after {
            addHtml("test.html", """
                <div data-sly-template.template="$DOLLAR{@ renamed}">
                    $DOLLAR{renamed @ context=renamed}
                </div>
            """)
        }
    }

    fun testUsedOptionRename() = renameCase {
        before {
            addHtml("test.html", """
                <div data-sly-template.template="$DOLLAR{ @ param}">
                    $DOLLAR{${CARET}param}
                </div>
            """)
        }
        renameTo("renamed")
        after {
            addHtml("test.html", """
                <div data-sly-template.template="$DOLLAR{ @ renamed}">
                    $DOLLAR{renamed}
                </div>
            """)
        }
    }

}