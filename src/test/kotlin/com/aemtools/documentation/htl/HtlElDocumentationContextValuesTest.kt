package com.aemtools.documentation.htl

import com.aemtools.blocks.documentation.BaseDocumentationTest
import com.aemtools.blocks.documentation.model.IDocTestFixture

/**
 * @author Dmytro Troynikov
 */
class HtlElDocumentationContextValuesTest
  : BaseDocumentationTest(HtlELDocumentationProvider()) {

  fun testDocHtml() = docCase {
    contextValue("html")

    documentation("""
            Use this in case you want to output HTML - Removes markup that may contain XSS risks.
        """)
  }

  fun testDocText() = docCase {
    contextValue("text")

    documentation("""
            Use this for simple HTML content - Encodes all HTML.
        """)
  }

  fun testDocElementName() = docCase {
    contextValue("elementName")

    documentation("""
            Allows only element names that are white-listed, outputs 'div' otherwise.
        """)
  }

  fun testDocAttributeName() = docCase {
    contextValue("attributeName")

    documentation("""
            Outputs nothing if the value doesn't correspond to the HTML attribute name syntax - doesn't allow 'style' and 'on*' attributes.
        """)
  }

  fun testDocAttribute() = docCase {
    contextValue("attribute")

    documentation("""
            Applies HTML attribute escaping
        """)
  }

  fun testDocUri() = docCase {
    contextValue("uri")

    documentation("""
            Outputs nothing if the value contains XSS risks.
        """)
  }

  fun testDocScriptToken() = docCase {
    contextValue("scriptToken")

    documentation("""
            Outputs nothing if the value doesn't correspond to the JavaScript token syntax.
        """)
  }

  fun testDocScriptString() = docCase {
    contextValue("scriptString")

    documentation("""
            Applies JavaScript string escaping.
        """)
  }

  fun testDocScriptComment() = docCase {
    contextValue("scriptComment")

    documentation("""
            Context for JavaScript block comments. Outputs nothing if value is trying to break out of the comment context.
        """)
  }

  fun testDocScriptRegExp() = docCase {
    contextValue("scriptRegExp")

    documentation("""
            Applies JavaScript regular expression escaping.
        """)
  }

  fun testDocStyleToken() = docCase {
    contextValue("styleToken")

    documentation("""
            Outputs nothing if the value doesn't correspond to the CSS token syntax.
        """)
  }

  fun testDocStyleString() = docCase {
    contextValue("styleString")

    documentation("""
            Applies CSS string escaping.
        """)
  }

  fun testDocStyleComment() = docCase {
    contextValue("styleComment")

    documentation("""
            Context for CSS comments. Outputs noting if value is trying to break out of the comment context.
        """)
  }

  fun testDocComment() = docCase {
    contextValue("comment")

    documentation("""
            Applies HTML comment escaping.
        """)
  }

  fun testDocNumber() = docCase {
    contextValue("number")

    documentation("""
            Outputs zero if the value is not a number.
        """)
  }

  fun testDocUnsafe() = docCase {
    contextValue("unsafe")

    documentation("""
            Use this at your own risk, this disables XSS protection completely.
        """)
  }

  private fun IDocTestFixture.contextValue(value: String) =
      this.addHtml("test.html", "$DOLLAR{@ context='$CARET$value'}")

}
