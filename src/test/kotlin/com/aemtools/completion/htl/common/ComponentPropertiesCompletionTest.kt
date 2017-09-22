package com.aemtools.completion.htl.common

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro Troynikov
 */
class ComponentPropertiesCompletionTest : CompletionBaseLightTest(true) {

  fun testPropertiesFromClassicDialog() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/components/comp/comp.html", """
            $DOLLAR{properties.$CARET}
        """)
    addXml("/$JCR_ROOT/apps/myapp/components/comp/dialog.xml", """
            <jcr:root jcr:primaryType="cq:Dialog">
                <items>
                    <item1 xtype="pathfield" name="./field1"/>
                    <item1 xtype="pathfield" name="field2"/>
                </items>
            </jcr:root>
        """)
    addXml("/$JCR_ROOT/apps/myapp/components/comp/.content.xml", "")
    shouldContain(
        listOf(
            "field1",
            "field2"
        ),
        false
    )
  }

  fun testPropertiesFromTouchUIDialog() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/components/comp/comp.html", """
            $DOLLAR{properties.$CARET}
        """)
    addXml("/$JCR_ROOT/apps/myapp/components/comp/.content.xml", "")
    addXml("/$JCR_ROOT/apps/myapp/components/comp/_cq_dialog/.content.xml", """
            <jcr:root>
                <items>
                    <item1 sling:resourceType="my/resource/type" name="./param1"/>
                    <item1 sling:resourceType="my/resource/type" name="param2"/>
                </items>
            </jcr:root>
        """)
    shouldContain(listOf(
        "param1",
        "param2"
    ), false)
  }

  fun testBothClassicAndTouchDialogsPresentTouchShouldWin() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/components/comp/comp.html", """
            $DOLLAR{properties.$CARET}
        """)
    addXml("/$JCR_ROOT/apps/myapp/components/comp/.content.xml", "")
    addXml("/$JCR_ROOT/apps/myapp/components/comp/dialog.xml", """
            <jcr:root jcr:primaryType="cq:Dialog">
                <items>
                    <item xtype="textfield" name="./classicField"/>
                </items>
            </jcr:root>
        """)
    addXml("/$JCR_ROOT/apps/myapp/components/comp/_cq_dialog/.content.xml", """
            <jcr:root>
                <items>
                    <item sling:resourceType="my/resource/type" name="./touchField"/>
                </items>
            </jcr:root>
        """)
    shouldContain(listOf("touchField"), false)
    shouldNotContain("classicField")
  }

}
