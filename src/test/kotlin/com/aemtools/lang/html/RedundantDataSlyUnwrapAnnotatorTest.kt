package com.aemtools.lang.html

import com.aemtools.lang.html.annotation.RedundantDataSlyUnwrapAnnotator.Companion.REDUNDANT_DATA_SLY_UNWRAP_MESSAGE
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

/**
 * @author Dmytro Troynikov
 */
class RedundantDataSlyUnwrapAnnotatorTest : LightCodeInsightFixtureTestCase() {

  fun testRedundantDataSlyUnwrap() {
    myFixture.configureByText("test.html", """
            <sly <warning descr="$REDUNDANT_DATA_SLY_UNWRAP_MESSAGE">data-sly-unwrap</warning>> </sly>
        """)
    myFixture.checkHighlighting()
  }

  fun testRedundantDataSlyUnwrap2() {
    myFixture.configureByText("test.html", """
            <sly class="test"
                 data-sly-use.<warning descr="null">bean</warning>="com.test.Bean"
                 <warning descr="$REDUNDANT_DATA_SLY_UNWRAP_MESSAGE">data-sly-unwrap</warning>>
            </sly>
        """)
    myFixture.checkHighlighting()
  }

  fun testNestedUnwrap() {
    myFixture.configureByText("test.html", """
            <sly>
                <div data-sly-unwrap></div>
            </sly>
        """)
    myFixture.checkHighlighting()
  }

}
