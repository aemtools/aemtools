package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for [HtlVariablesAnnotator] to check data-sly-BLOCK scope in HTL v 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class HtlVariablesScopeAnnotatorV13Test : HtlVariablesScopeAnnotatorTest() {
  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test variable usage after declaration in the tag with unsupported data-sly-set`() {
    myFixture.configureByText("test.html", """
        <div>
          <sly <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">data-sly-set</error>.var1=""/>
          $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
        </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage after declaration in the unsupported data-sly-unwrap`() {
    myFixture.configureByText("test.html", """
        <div>
          <sly <info descr="null">data-sly-unwrap</info>.<error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">var1</error>=<error descr="Support for this option starts with HTL version 1.3.">""</error>/>
          $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
        </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}
