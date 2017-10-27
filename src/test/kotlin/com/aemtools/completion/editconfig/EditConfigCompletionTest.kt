package com.aemtools.completion.editconfig

import com.aemtools.blocks.completion.CompletionBaseLightTest

/**
 * @author Dmytro Troynikov
 */
class EditConfigCompletionTest : CompletionBaseLightTest(false) {

  fun testBaseCompletion() = completionTest {
    addXml("_cq_editConfig.xml", """
        <jcr:root
            $CARET>
        </jcr:root>
        """.trimIndent())
    shouldContain(
        "jcr:primaryType",
        "cq:actions",
        "cq:layout",
        "cq:dialogMode",
        "cq:emptyText",
        "cq:inherit"
    )
  }

  fun testFilterOutDuplicates() = completionTest {
    addXml("_cq_editConfig.xml", """
            <jcr:root jcr:primaryType='cq:EditConfig'
                    $CARET></jcr:root>
        """)
    shouldNotContain("jcr:primaryType")
  }

  fun testPrimaryType() = completionTest {
    addXml("_cq_editConfig.xml", """
            <jcr:root jcr:primaryType='$CARET'></jcr:root>
        """)
    shouldContain("cq:EditConfig")
  }

  fun testCqActions() = completionTest {
    addXml("_cq_editConfig.xml", """
            <jcr:root cq:actions='$CARET'></jcr:root>
        """)
    shouldContain(emptyList())
  }

  fun testCqLayout() = completionTest {
    addXml("_cq_editConfig.xml", """
            <jcr:root cq:layout='$CARET'></jcr:root>
        """)
    shouldContain(
        "rollover",
        "editbar",
        "auto"
    )
  }

  fun testCqDialogMode() = completionTest {
    addXml("_cq_editConfig.xml", """
            <jcr:root cq:dialogMode='$CARET'></jcr:root>
        """)
    shouldContain(
        "floating",
        "inline",
        "auto"
    )
  }

  fun testCqInherit() = completionTest {
    addXml("_cq_editConfig.xml", """
            <jcr:root cq:inherit='$CARET'></jcr:root>
        """)
    shouldContain(
        "true",
        "false"
    )
  }

  // todo fix test
//    fun testRootChildNodes() = completionTest {
//        addXml("_cq_editConfig.xml", """
//            <jcr:root>
//                $CARET
//            </jcr:root>
//        """)
//        shouldContain(
//                "cq:dropTargets",
//                "cq:actionConfigs",
//                "cq:formParameters",
//                "cq:inplaceEditing",
//                "cq:listeners"
//        )
//    }

}
