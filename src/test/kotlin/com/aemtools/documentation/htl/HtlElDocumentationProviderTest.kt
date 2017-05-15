package com.aemtools.documentation.htl

import com.aemtools.blocks.documentation.BaseDocumentationTest

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

}