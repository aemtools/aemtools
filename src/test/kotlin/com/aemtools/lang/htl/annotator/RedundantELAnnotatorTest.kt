package com.aemtools.lang.htl.annotator

import com.aemtools.constant.const.DOLLAR
import com.aemtools.constant.messages.annotator.SIMPLIFY_EXPRESSION
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

/**
 * @author Dmytro_Troynikov
 */
class RedundantELAnnotatorTest : LightCodeInsightFixtureTestCase() {

  fun testRedundantELInDataSlyUse() {
    myFixture.configureByText("test.html", """
            <div data-sly-use="<warning descr="$SIMPLIFY_EXPRESSION">$DOLLAR{'com.test.Bean'}</warning>"></div>
        """)
    myFixture.checkHighlighting()
  }

  fun testRedundantELInDataSlyUseNotReportedInPresenceOfOptions() {
    myFixture.configureByText("test.html", """
            <div data-sly-use="$DOLLAR{'com.test.Bean' @ param='value'}"></div>
        """)
    myFixture.checkHighlighting()
  }

  fun testRedundantELInDataSlyInclude() {
    myFixture.configureByText("test.html", """
            <div data-sly-include="<warning descr="$SIMPLIFY_EXPRESSION">$DOLLAR{'template.html'}</warning>"></div>
        """)
    myFixture.checkHighlighting()
  }

  fun testRedundantELInDataSlyIncludeNotReportedInPresenceOfOptions() {
    myFixture.configureByText("test.html", """
            <div data-sly-include="$DOLLAR{'template' @ param='value'}"></div>
        """)
  }

}
