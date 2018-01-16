package com.aemtools.codeinsight.htl.annotator

import com.aemtools.test.base.BaseLightTest

/**
 * Test for [HtlWrongQuotesAnnotator].
 *
 * @author Dmytro Troynikov
 */
class HtlWrongQuotesAnnotatorTest : BaseLightTest() {

  fun testWrongSingleQuotes() {
    myFixture.configureByText("test.html", """
      <div attribute='$DOLLAR{<error descr="Incorrect quotes">'wrong'</error>}'></div>
    """)

    myFixture.checkHighlighting()
  }

  fun testWrongSingleQuotesInSecondExpression() {
    myFixture.configureByText("test.html", """
      <div attribute='$DOLLAR{"correct"} $DOLLAR{<error descr="Incorrect quotes">'wrong'</error>}'></div>
    """)

    myFixture.checkHighlighting()
  }

  fun testWrongDoubleQuotes() {
    myFixture.configureByText("test.html", """
      <div attribute="$DOLLAR{<error descr="Incorrect quotes">"wrong"</error>}"></div>
    """)

    myFixture.checkHighlighting()
  }

  fun testWrongDoubleQuotesInSecondExpression() {
    myFixture.configureByText("test.html", """
      <div attribute="$DOLLAR{'correct'} $DOLLAR{<error descr="Incorrect quotes">"wrong"</error>}"></div>
    """)

    myFixture.checkHighlighting()
  }

}
