package com.aemtools.documentation.htl

import com.aemtools.blocks.documentation.BaseDocumentationTest
import com.aemtools.blocks.documentation.model.IDocTestFixture

/**
 * @author Dmytro Troynikov
 */
class HtlElDocumentationListHelperTest :
    BaseDocumentationTest(HtlELDocumentationProvider()) {

  fun testIndexDocumentation() = docCase {
    listField("index")

    documentation("""
            zero-based counter (0..length-1)
        """)
  }

  fun testCountDocumentation() = docCase {
    listField("count")

    documentation("""
            one-based counter (1..length)
        """)
  }

  fun testFirstDocumentation() = docCase {
    listField("first")

    documentation("""
            <b>true</b> for the first element being iterated
        """)
  }

  fun testMiddleDocumentation() = docCase {
    listField("middle")

    documentation("""
            <b>true</b> if element being iterated is neither the first nor the last
        """)
  }

  fun testLastDocumentation() = docCase {
    listField("last")

    documentation("""
            <b>true</b> for the last element being iterated
        """)
  }

  fun testOddDocumentation() = docCase {
    listField("odd")

    documentation("""
            <b>true</b> if index is odd
        """)
  }

  fun testEvenDocumentation() = docCase {
    listField("even")

    documentation("""
            <b>true</b> if index is even
        """)
  }

  fun testDocForDataSlyRepeat() = docCase {
    addHtml("test.html", """
            <div data-sly-repeat="$DOLLAR{}">
                $DOLLAR{itemList.${CARET}index}
            </div>
        """)

    documentation("""
            zero-based counter (0..length-1)
        """)
  }

  fun testDocRenamedItemListInDataSlyList() = docCase {
    addHtml("test.html", """
            <div data-sly-list.renamed="$DOLLAR{}">
                $DOLLAR{renamedList.${CARET}index}
            </div>
        """)

    documentation("""
            zero-based counter (0..length-1)
        """)
  }

  fun testDocRenamedItemListInDataSlyRepeat() = docCase {
    addHtml("test.html", """
            <div data-sly-repeat.renamed="$DOLLAR{}">
                $DOLLAR{renamedList.${CARET}index}
            </div>
        """)

    documentation("""
            zero-based counter (0..length-1)
        """)
  }

  private fun IDocTestFixture.listField(field: String) =
      addHtml("test.html", """
                <div data-sly-list="$DOLLAR{}">$DOLLAR{itemList.$CARET$field}</div>
            """)

}
