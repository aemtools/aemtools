package com.aemtools.documentation.html

import com.aemtools.test.documentation.BaseDocumentationTest

/**
 * Test for [HtmlLinkCheckerDocumentationProvider].
 *
 * @author Dmytro Primshyts
 */
class HtmlLinkCheckerDocumentationProviderTest
  : BaseDocumentationTest(HtmlLinkCheckerDocumentationProvider()) {

  fun testLinkCheckerAttributeDoc() = docCase {
    addHtml("test.html", """
        <a ${CARET}x-cq-linkchecker=''></a>
    """)

    documentation("Link checker configuration")
  }

  fun testSkipValue() = docCase {
    addHtml("test.html", """
       <a x-cq-linkchecker="${CARET}skip"></a>
    """)
    documentation("This link will be ignored by Link checker")
  }

  fun testValidValue() = docCase {
    addHtml("test.html", """
       <a x-cq-linkchecker="${CARET}valid"></a>
    """)
    documentation("Link checker will check this link and mark as valid")
  }

}
