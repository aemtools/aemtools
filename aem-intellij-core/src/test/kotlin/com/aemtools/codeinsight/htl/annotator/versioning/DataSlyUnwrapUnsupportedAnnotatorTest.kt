package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest

/**
 * Tests for [DataSlyUnwrapUnsupportedAnnotator].
 *
 * @author Kostiantyn Diachenko
 */
class DataSlyUnwrapUnsupportedAnnotatorTest : BaseLightTest() {
  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test annotating of unsupported identifier and value of data-sly-unwrap attribute`() {
    myFixture.configureByText("test.html", """
      <sly <info descr="null">data-sly-unwrap</info>.<error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">var1</error>=<weak_warning descr="This expression has no effect in current HTL version 1.3. Support for this feature starts with HTL version 1.4.">""</weak_warning>/>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of unsupported identifier of data-sly-unwrap attribute`() {
    myFixture.configureByText("test.html", """
      <sly <info descr="null">data-sly-unwrap</info>=<weak_warning descr="This expression has no effect in current HTL version 1.3. Support for this feature starts with HTL version 1.4.">""</weak_warning>/>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of unsupported value of data-sly-unwrap attribute`() {
    myFixture.configureByText("test.html", """
      <sly <info descr="null">data-sly-unwrap</info>.<error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">var1</error>/>
      $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of supported identifier of data-sly-unwrap attribute`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)
    myFixture.configureByText("test.html", """
      <sly <info descr="null">data-sly-unwrap</info>.<weak_warning descr="null">var1</weak_warning>=""/>
      $DOLLAR{<weak_warning descr="null">var1</weak_warning>}
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}
