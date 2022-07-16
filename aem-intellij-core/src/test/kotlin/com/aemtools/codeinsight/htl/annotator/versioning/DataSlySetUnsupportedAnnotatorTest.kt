package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest

/**
 * Tests for [DataSlySetUnsupportedAnnotator].
 *
 * @author Kostiantyn Diachenko
 */
class DataSlySetUnsupportedAnnotatorTest : BaseLightTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test annotating of unsupported data-sly-set attribute`() {
    myFixture.configureByText("test.html", """
      <sly <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">data-sly-set</error>.var1=""/>
      $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of supported data-sly-set attribute`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)
    myFixture.configureByText("test.html", """
      <sly <info descr="null">data-sly-set</info>.<weak_warning descr="null">var1</weak_warning>=""/>
      $DOLLAR{<weak_warning descr="null">var1</weak_warning>}
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}

