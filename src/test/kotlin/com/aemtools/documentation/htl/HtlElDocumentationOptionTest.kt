package com.aemtools.documentation.htl

import com.aemtools.blocks.documentation.BaseDocumentationTest
import com.aemtools.blocks.documentation.model.IDocTestFixture
import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Troynikov
 */
class HtlElDocumentationOptionTest
  : BaseDocumentationTest(HtlELDocumentationProvider()) {

  fun testDocForOptionNameContext() = docCase {
    option("context")

    documentation("""
            Override automatically recognized context.
        """)
  }

  fun testDocForOptionNameFormat() = docCase {
    option("format")

    documentation("""
            Formats the main string.
        """)
  }

  fun testDocForOptionNameI18n() = docCase {
    option("scheme")

    documentation("""
            Applies given scheme to URI set within the main string.
        """)
  }

  fun testDocForOptionNameJoin() = docCase {
    option("join")

    documentation("""
            Controls the output of an array object using given separator string.
        """)
  }

  fun testDocForOptionNameScheme() = docCase {
    option("scheme")

    documentation("""
            Applies given scheme to URI set within the main string.
        """)
  }

  fun testDocForOptionNameDomain() = docCase {
    option("domain")

    documentation("""
            Applies given domain to URI set within the main string.
        """)
  }

  fun testDocForOptionNamePath() = docCase {
    option("path")

    documentation("""
            Modifies the path that identifies a resource.
        """)
  }

  fun testDocForOptionNamePrependPath() = docCase {
    option("prependPath")

    documentation("""
            Prepends its content to the path that identifies a resource.
        """)
  }

  fun testDocForOptionNameAppendPath() = docCase {
    option("appendPath")

    documentation("""
            Appends its content to the path that identifies a resource.
        """)
  }

  fun testDocForOptionNameSelectors() = docCase {
    option("selectors")

    documentation("""
            Modifies or removes the selectors from a URI; the selectors are the URI segments between the part that identifies a resource (the resource's path) and the extension used for representing the resource.
        """)
  }

  fun testDocForOptionAddSelectors() = docCase {
    option("addSelectors")

    documentation("""
            Add the provided selectors (selectors string or selectors array) to the URI.
        """)
  }

  fun testDocForOptionRemoveSelectors() = docCase {
    option("removeSelectors")

    documentation("""
            Remove the provided selectors (selectors string or selectors array) from the URI.
        """)
  }

  fun testDocForOptionExtension() = docCase {
    option("extension")

    documentation("""
            Apply given extension to a URI.
        """)
  }

  fun testDocForOptionSuffix() = docCase {
    option("suffix")

    documentation("""
            Apply given suffix to a URI.
        """)
  }

  fun testDocForOptionPrependSuffix() = docCase {
    option("prependSuffix")

    documentation("""
            Prepend suffix to a URI.
        """)
  }

  fun testDocForOptionAppendSuffix() = docCase {
    option("appendSuffix")

    documentation("""
            Append suffix to a URI.
        """)
  }

  fun testDocForOptionQuery() = docCase {
    option("query")

    documentation("""
            Set the query segment of a URI.
        """)
  }

  fun testDocForOptionAddQuery() = docCase {
    option("addQuery")

    documentation("""
            Add given query to the query segment of a URI.
        """)
  }

  fun testDocForOptionRemoveQuery() = docCase {
    option("removeQuery")

    documentation("""
            Remove identified parameters from an existing query segment of a URI.
        """)
  }

  fun testDocForOptionFragment() = docCase {
    option("fragment")

    documentation("""
            Set the fragment segment of a URI.
        """)
  }

  private fun IDocTestFixture.option(@Language("Htl") option: String) =
      this.addHtml("test.html", "$DOLLAR{@ $CARET$option}")

}
