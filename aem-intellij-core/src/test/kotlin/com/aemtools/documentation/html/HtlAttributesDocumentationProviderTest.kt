package com.aemtools.documentation.html

import com.aemtools.test.documentation.BaseDocumentationTest
import com.aemtools.test.documentation.model.IDocTestFixture

/**
 * @author Dmytro Primshyts
 */
abstract class HtlAttributesDocumentationProviderTest
  : BaseDocumentationTest(HtlAttributesDocumentationProvider()) {

  fun IDocTestFixture.htlAttribute(attribute: String) =
      this.addHtml("test.html", """
                <div $CARET$attribute=""></div>
            """)

}
