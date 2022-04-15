package com.aemtools.reference.htl

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.reference.BaseReferenceTest
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro Primshyts
 */
class DialogPropertiesReferenceTest : BaseReferenceTest(true) {

  fun testReferenceToPropertyFromClassicDialog() = testReference {
    addHtml("/$JCR_ROOT/apps/myapp/component/component.html", """
            $DOLLAR{properties.${CARET}property}
        """)
    addXml("/$JCR_ROOT/apps/myapp/component/dialog.xml", """
            <jcr:root jcr:primaryType="cq:Dialog">
                <item xtype="pathfield" name="./property"/>
            </jcr:root>
        """)
    addXml("/$JCR_ROOT/apps/myapp/component/.content.xml", "")

    shouldResolveTo(XmlTag::class.java)
    shouldContainText("""<item xtype="pathfield" name="./property"/>""")
  }

  fun testReferenceToPropertyFromTouchDialog() = testReference {
    addHtml("/$JCR_ROOT/apps/myapp/component/component.html", """
            $DOLLAR{properties.${CARET}property}
        """)
    addXml("/$JCR_ROOT/apps/myapp/component/_cq_dialog/.content.xml", """
            <jcr:root>
                <item sling:resourceType="granite/ui/components/foundation/form/textfield"
                name="./property"/>
            </jcr:root>
        """)
    addXml("/$JCR_ROOT/apps/myapp/component/.content.xml", "")

    shouldResolveTo(XmlTag::class.java)
    shouldContainText("""<item sling:resourceType="granite/ui/components/foundation/form/textfield"
                name="./property"/>""")

  }

}
