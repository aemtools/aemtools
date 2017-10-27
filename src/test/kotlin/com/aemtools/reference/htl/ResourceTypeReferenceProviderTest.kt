package com.aemtools.reference.htl

import com.aemtools.blocks.reference.BaseReferenceTest
import com.aemtools.constant.const.JCR_ROOT
import com.intellij.psi.PsiDirectory

/**
 * @author Dmytro Troynikov
 */
class ResourceTypeReferenceProviderTest : BaseReferenceTest() {

  fun testResourceTypeReference() = testReference {
    addHtml("/$JCR_ROOT/apps/component1/test.html", """
            <div data-sly-resource="$DOLLAR{'name' @ resourceType='$CARET/apps/component'}"
        """)
    addXml("/$JCR_ROOT/apps/component/.content.xml", """
            <jcr:root
                jcr:primaryType="cq:Component"
                componentGroup="My group"
                jcr:title="My Component"/>
        """)
    shouldResolveTo(PsiDirectory::class.java)
  }

  fun testResourceTypeNotFullReference() = testReference {
    addHtml("/$JCR_ROOT/apps/myapp/components/test/test.html", """
            <div data-sly-resource="$DOLLAR{'name' @ resourceType='${CARET}myapp/components/included'}"></div>
        """)

    addXml("/$JCR_ROOT/apps/myapp/components/included/.content.xml", """
            <jcr:root
                jcr:primaryType="cq:Component"
                componentGroup="My group"
                jcr:title="My Component" />
        """)

    shouldResolveTo(PsiDirectory::class.java)
  }

  fun testResourceTypeProjectRelativeReference() = testReference {
    addHtml("/$JCR_ROOT/apps/myapp/components/test/test.html", """
            <div data-sly-resource="$DOLLAR{'name' @ resourceType='${CARET}components/included'}"></div>
        """)

    addXml("/$JCR_ROOT/apps/myapp/components/included/.content.xml", """
            <jcr:root
                jcr:primaryType="cq:Component"
                componentGroup="My Group"
                jcr:title="My component" />
        """)

    shouldResolveTo(PsiDirectory::class.java)
  }

}
