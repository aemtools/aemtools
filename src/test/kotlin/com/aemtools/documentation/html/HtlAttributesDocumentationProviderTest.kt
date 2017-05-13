package com.aemtools.documentation.html

import com.aemtools.blocks.documentation.BaseDocumentationTest

/**
 * @author Dmytro Troynikov
 */
class HtlAttributesDocumentationProviderTest
    : BaseDocumentationTest(HtlAttributesDocumentationProvider()) {

    fun testDataSlyUseDocumentation() = docCase {
        addHtml("test.html", """
            <div ${CARET}data-sly-use=''></div>
        """)

        documentation("""
            <h2>data-sly-use</h2>
            <b>Description:</b> Exposes logic to the template<br>
            <b>Element:</b> always shown<br>
            <b>Attribute value:</b><br>
             - required: true<br>
             - type: String<br>
             - description: the object to instantiate.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: Customised identifier name to access the instantiated logic<br><br><br>See also: <a href="https://github.com/Adobe-Marketing-Cloud/htl-spec/blob/master/SPECIFICATION.md#221-use">Htl Specification</a>
        """)
    }

}
