package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest

/**
 * @author Dmytro Troynikov
 */
class JavaToHtlRenameTest : BaseRenameTest() {

    fun testMethodRenameFromJava() = renameCase {
        before {
            addClass("com/test/Bean.java", """
                package com.test;
                public class Bean {
                    public String ${CARET}getField() { return ""; }
                }
            """)

            addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.field}
                </div>
            """)
        }
        renameTo("getRenamed")
        after {
            addClass("com/test/Bean.java", """
                package com.test;
                public class Bean {
                    public String getRenamed() { return ""; }
                }
            """)
            addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.renamed}
                </div>
            """)
        }
    }

}