package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest

/**
 * @author Dmytro Troynikov
 */
class HtlVariablesRenameTest : BaseRenameTest() {

    fun testRenameDataSlyUseVariable() = renameCase {
        before {
            addHtml("test.html", """
                <div data-sly-use.bean='111'>
                    $DOLLAR{${CARET}bean}
                </div>
            """)
        }
        renameTo("renamed")
        after {
            addHtml("test.html", """
                <div data-sly-use.renamed='111'>
                    $DOLLAR{renamed}
                </div>
            """)
        }
    }

    fun testRenameDataSlyUseVariableFromAttribute() = renameCase {
        before {
            addHtml("test.html", """
                <div data-sly-use.${CARET}bean='111'>
                    $DOLLAR{bean}
                </div>
            """)
        }
        renameTo("renamed")
        after {
            addHtml("test.html", """
                <div data-sly-use.renamed='111'>
                    $DOLLAR{renamed}
                </div>
            """)
        }
    }

    fun testRenameDataSlyTestVariable() = renameCase {
        before {
            addHtml("test.html", """
                <div data-sly-test.test="$DOLLAR{properties}">
                    $DOLLAR{${CARET}test}
                </div>
            """)
        }
        renameTo("renamed")
        after {
            addHtml("test.html", """
                <div data-sly-test.renamed="$DOLLAR{properties}">
                    $DOLLAR{renamed}
                </div>
            """)
        }
    }

    fun testRenameDataSlyTestFromAttribute() = renameCase {
        before {
            addHtml("test.html", """
                <div data-sly-test.${CARET}test="$DOLLAR{properties}">
                    $DOLLAR{${CARET}test}
                </div>
            """)
        }
        renameTo("renamed")
        after {
            addHtml("test.html", """
                <div data-sly-test.renamed="$DOLLAR{properties}">
                    $DOLLAR{renamed}
                </div>
            """)
        }
    }

}