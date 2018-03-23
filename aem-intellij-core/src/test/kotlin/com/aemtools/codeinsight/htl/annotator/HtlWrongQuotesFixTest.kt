package com.aemtools.codeinsight.htl.annotator

import com.aemtools.test.fix.BaseFixTest

/**
 * Tests for:
 *
 *  * [HtlWrongQuotesAnnotator]
 *  * [HtlWrongQuotesLiteralFixIntentionAction]
 *  * [HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction]
 *
 * @author Dmytro Troynikov
 */
class HtlWrongQuotesFixTest : BaseFixTest() {

  fun `test Invert HTL Literal quotes with doublequotes`() = annotationFixTest {
    fixName = "Invert HTL Literal quotes"

    before = html("test.html", """
        <div attribute="$DOLLAR{"wrong'\""}"></div>
    """)

    after = html("test.html", """
        <div attribute="$DOLLAR{'wrong\'"'}"></div>
    """)
  }

  fun `test Invert HTL Literal quotes with singlequotes`() = annotationFixTest {
    fixName = "Invert HTL Literal quotes"

    before = html("test.html", """
      <div attribute='$DOLLAR{'wrong\'"'}'></div>
    """)
    after = html("test.html", """
      <div attribute='$DOLLAR{"wrong'\""}'></div>
    """)
  }

  fun `test Invert XML Attribute quotes with doublequotes`() = annotationFixTest {
    fixName = "Invert XML Attribute quotes"

    before = html("test.html", """
      <div attr="$DOLLAR{"wrong\"'" @ a='a\'"'}
        $DOLLAR{'correct'}
        $DOLLAR{"wrong"}
      "></div>
    """)

    after = html("test.html", """
      <div attr='$DOLLAR{"wrong\"'" @ a="a'\""}
        $DOLLAR{"correct"}
        $DOLLAR{"wrong"}
      '></div>
    """)
  }

  fun `test Invert XML Attribute quotes with singlequotes`() = annotationFixTest {
    fixName = "Invert XML Attribute quotes"

    before = html("test.html", """
      <div attr='$DOLLAR{'wrong\'"' @ a="a\"'"}
        $DOLLAR{"correct"}
        $DOLLAR{'wrong'}
      '></div>
    """)

    after = html("test.html", """
      <div attr="$DOLLAR{'wrong\'"' @ a='a"\''}
        $DOLLAR{'correct'}
        $DOLLAR{'wrong'}
      "></div>
    """)
  }

}
