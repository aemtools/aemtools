package com.aemtools.reference.htl

import com.aemtools.blocks.base.BaseLightTest.Companion.CARET
import com.aemtools.blocks.base.BaseLightTest.Companion.DOLLAR
import com.aemtools.blocks.reference.BaseReferenceTest
import com.intellij.psi.PsiDirectory

/**
 * TODO: fix test
 *
 * @author Dmytro Troynikov
 */
abstract class ResourceTypeReferenceProviderTest : BaseReferenceTest() {

    fun testResourceTypeReference() = testReference {
        addHtml("/jcr_root/apps/component1/test.html", """
            <div data-sly-resource="$DOLLAR{'name' @ resourceType='$CARET/apps/component'}"
        """)
        addXml("/jcr_root/apps/component/.content.xml", """
            <jcr:root
                jcr:primaryType="cq:Component"
                componentGroup="My group"
                jcr:title="My Component"/>
        """)
        shouldResolveTo(PsiDirectory::class.java)
        shouldContainText("component")
    }

}