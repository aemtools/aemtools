package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest

/**
 * Tests for [DataSlyIterableOptionsUnsupportedAnnotator].
 *
 * @author Kostiantyn Diachenko
 */
class DataSlyIterableOptionsUnsupportedAnnotatorTest : BaseLightTest() {

  fun `test annotating of unsupported data-sly-list options`() {
    myFixture.setHtlVersion(HtlVersion.V_1_3)
    myFixture.configureByText("test.html", """
      <div <info descr="null">data-sly-list</info>="$DOLLAR{ @ <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">begin</error>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of unsupported data-sly-repeat options`() {
    myFixture.setHtlVersion(HtlVersion.V_1_3)
    myFixture.configureByText("test.html", """
      <div <info descr="null">data-sly-repeat</info>="$DOLLAR{ @ <error descr="Support for this option starts with HTL version 1.4. Current project version - 1.3.">step</error>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of supported data-sly-list options`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)
    myFixture.configureByText("test.html", """
        <div <info descr="null">data-sly-repeat</info>="$DOLLAR{ @ <weak_warning descr="Iterable Parameter">end</weak_warning>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test annotating of supported data-sly-repeat options`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)
    myFixture.configureByText("test.html", """
        <div <info descr="null">data-sly-list</info>="$DOLLAR{ @ <weak_warning descr="Iterable Parameter">step</weak_warning>=''}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}
