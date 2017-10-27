package com.aemtools.reference.htl

import com.aemtools.blocks.reference.BaseReferenceTest
import com.aemtools.constant.const.JCR_ROOT
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class TemplateReferenceTest : BaseReferenceTest() {

  fun testReferenceToImportedTemplateByRelativePath() = testReference {
    addHtml("/$JCR_ROOT/apps/myapp/component/component.html", """
            <div data-sly-use.template="template.html">
                <div data-sly-call="$DOLLAR{template.${CARET}template}"></div>
            </div>
        """)
    addHtml("/$JCR_ROOT/apps/myapp/component/template.html", """
            <div data-sly-template.template></div>
        """)

    shouldResolveTo(XmlAttribute::class.java)
    shouldContainText("data-sly-template.template")
  }

  fun testReferenceToImportedTemplateByAbsolutePath() = testReference {
    addHtml("/$JCR_ROOT/apps/myapp/component/component.html", """
            <div data-sly-use.template="/apps/myapp/templates/template.html">
                <div data-sly-call="$DOLLAR{template.${CARET}template"></div>
            </div>
        """)
    addHtml("/$JCR_ROOT/apps/myapp/templates/template.html", """
            <div data-sly-template.template></div>
        """)

    shouldResolveTo(XmlAttribute::class.java)
    shouldContainText("data-sly-template.template")
  }

}
