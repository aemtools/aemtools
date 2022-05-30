package com.aemtools.completion.htl.common

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.BaseVariantsCheckContributorTest.Companion.OBJECT_VARIANTS
import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for com.day.cq.wcm.api.components.Component object completion in HTL.
 *
 * @author Kostiantyn Diachenko
 */
class WcmApiComponentCompletionTest : CompletionBaseLightTest(true) {

  fun testPropertiesFromClassicDialog() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/components/comp/comp.html", """
            $DOLLAR{component.properties.$CARET}
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
            $DOLLAR{component.properties.$CARET}
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
            $DOLLAR{component.properties.$CARET}
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

  fun testComponentCompletionVariants() = completionTest {
    addHtml("/$JCR_ROOT/apps/myapp/components/comp/comp.html", """
            $DOLLAR{component.$CARET}
        """)

    shouldContain(listOf(
        "accessible",
        "analyzable",
        "cellName",
        "childEditConfig",
        "componentGroup",
        "container",
        "declaredChildEditConfig",
        "declaredEditConfig",
        "defaultView",
        "designable",
        "designDialogPath",
        "dialogPath",
        "editable",
        "editConfig",
        "htmlTagAttributes",
        "iconPath",
        "infoProviders",
        "noDecoration",
        "properties",
        "resourceType",
        "superComponent",
        "templatePath",
        "thumbnailPath",
        "description",
        "name",
        "path",
        "title"
    ) + OBJECT_VARIANTS, false)
  }
}
