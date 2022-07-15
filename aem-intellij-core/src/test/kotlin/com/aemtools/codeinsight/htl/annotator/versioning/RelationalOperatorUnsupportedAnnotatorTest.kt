package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest

/**
 * Tests for [RelationalOperatorUnsupportedAnnotator].
 *
 * @author Kostiantyn Diachenko
 */
class RelationalOperatorUnsupportedAnnotatorTest : BaseLightTest() {
  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test annotating of unsupported relational operator with string literal`() {
    myFixture.configureByText("test.html", """
        <sly data-sly-test.<warning descr="null">check</warning>="$DOLLAR{'1' <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">in</error> '123'}"/>
    """)
    myFixture.checkHighlighting(true, false, false)
  }

  fun `test annotating of unsupported relational operator with array literal`() {
    myFixture.configureByText("test.html", """
        <sly data-sly-test.<warning descr="null">check</warning>="$DOLLAR{100 <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">in</error> [100, 200]}"/>
    """)
    myFixture.checkHighlighting(true, false, false)
  }

  fun `test annotating of unsupported relational operator with property access`() {
    myFixture.configureByText("test.html", """
        <sly data-sly-test.var="$DOLLAR{[100, 200]}"/>
        <sly data-sly-test.<warning descr="null">check</warning>="$DOLLAR{100 <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">in</error> var}"/>
    """)
    myFixture.checkHighlighting(true, false, false)
  }

  fun `test error in parser with not relational operator operand after relational operator`() {
    myFixture.configureByText("test.html", """
       <sly data-sly-test.<warning descr="null">check</warning>="$DOLLAR{100 <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">in</error> 100}"/>
    """)
    myFixture.checkHighlighting(true, false, false)
  }

  fun `test not annotating of relational operator`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)

    myFixture.configureByText("test.html", """
        <sly data-sly-test.<warning descr="null">check</warning>="$DOLLAR{'1' in '123'}"/>
    """)
    myFixture.checkHighlighting(true, false, false)
  }
}
