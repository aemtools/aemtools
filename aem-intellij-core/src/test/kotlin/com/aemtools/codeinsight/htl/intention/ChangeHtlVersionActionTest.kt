package com.aemtools.codeinsight.htl.intention

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import org.assertj.core.api.Assertions

/**
 * Tests for [ChangeHtlVersionAction].
 *
 * @author Kostiantyn Diachenko
 */
class ChangeHtlVersionActionTest : BaseLightTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test not empty change HTL version action`() {
    myFixture.configureByText("test.html", """
      <div data-sly-set.variable=""></div>
    """)

    val fix by notNull<ChangeHtlVersionAction> {
      myFixture.quickFix("test.html")
    }

    Assertions.assertThat(fix.familyName).isEqualTo("HTL Intentions")
    Assertions.assertThat(fix.startInWriteAction()).isFalse
    Assertions.assertThat(fix.text).isEqualTo("Change HTL version")
    Assertions.assertThat(fix.isAvailable(project, editor, null)).isTrue
  }
}
