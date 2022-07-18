package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.util.writeCommand
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import org.assertj.core.api.Assertions

/**
 * Tests for [ReplaceDataSlySetWithDataSlyTestAction].
 *
 * @author Kostiantyn Diachenko
 */
class ReplaceDataSlySetWithDataSlyTestActionTest : BaseLightTest() {
  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test not empty action`() {
    myFixture.configureByText("test.html", """
      <div data-sly-set.var1="value"></div>
    """)

    val fix by notNull<ReplaceDataSlySetWithDataSlyTestAction> {
      myFixture.quickFix("test.html")
    }

    Assertions.assertThat(fix.familyName)
        .isEqualTo("HTL Intentions")

    Assertions.assertThat(fix.startInWriteAction()).isTrue

    Assertions.assertThat(fix.text)
        .isEqualTo("Replace with data-sly-test")

    Assertions.assertThat(fix.isAvailable(project, editor, null)).isTrue
  }

  fun `test absent action in HTL 1_4`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)

    myFixture.configureByText("test.html", """
      <div data-sly-set.var1="value"></div>
    """)

    val fix = myFixture.quickFix<ReplaceDataSlySetWithDataSlyTestAction>("test.html")

    Assertions.assertThat(fix).isNull()
  }

  fun `test applying action for data-sly-unwrap with identifier`() {
    myFixture.configureByText("test.html", """
      <div data-sly-set.var1="value"></div>
    """)

    val fix by notNull<ReplaceDataSlySetWithDataSlyTestAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div data-sly-test.var1="value"></div>
    """)
  }

  fun `test applying action for data-sly-set`() {
    myFixture.configureByText("test.html", """
      <div data-sly-set="value"></div>
    """)

    val fix by notNull<ReplaceDataSlySetWithDataSlyTestAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div data-sly-test="value"></div>
    """)
  }
}
