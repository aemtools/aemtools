package com.aemtools.documentation.htl

import com.aemtools.blocks.documentation.BaseDocumentationTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro Troynikov
 */
class HtlElDocumentationProviderTest
  : BaseDocumentationTest(HtlELDocumentationProvider(), true) {

  fun testDocForPredefinedProperty() = docCase {
    addHtml("test.html", """
            $DOLLAR{properties.${CARET}jcr:title}
        """)

    documentation("""
            String value of <b>jcr:title</b> property, or empty String if such property does not exist.
        """)
  }

  fun testDocForResourceType() = docCase {
    addHtml("/$JCR_ROOT/apps/myapp/components/comp/comp.html", """
            <div data-sly-resource="$DOLLAR{'path' @ resourceType='$CARET/apps/myapp/components/comp1'}"></div>
        """)
    addXml("/$JCR_ROOT/apps/myapp/components/comp1/.content.xml", """
            <jcr:root jcr:primaryType="cq:Component" componentGroup="My Group" jcr:title="My title"/>
        """)
    documentation("""
            AEM Component:<br/>
            <b>Name</b>: comp1<br/>
            <b>Group</b>: My Group<br/>
            <b>jcr:title</b>: My title<br/>
            <b>Container</b>: false<br/>
        """)
  }

}
