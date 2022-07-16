package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.util.writeCommand
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import org.assertj.core.api.Assertions

/**
 * Tests for [RemoveRedundantDataSlyUnwrapValueAction].
 *
 * @author Kostiantyn Diachenko
 */
class RemoveRedundantDataSlyUnwrapValueActionTest : BaseLightTest() {
  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test not empty action`() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap="$DOLLAR{}"></div>
    """)

    val fix by notNull<RemoveRedundantDataSlyUnwrapValueAction> {
      myFixture.quickFix("test.html")
    }

    Assertions.assertThat(fix.familyName)
        .isEqualTo("HTL Intentions")

    Assertions.assertThat(fix.startInWriteAction()).isTrue

    Assertions.assertThat(fix.text)
        .isEqualTo("Remove data-sly-unwrap attribute value")

    Assertions.assertThat(fix.isAvailable(project, editor, null)).isTrue
  }

  fun `test absent action in HTL 1_4`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)

    myFixture.configureByText("test.html", """
      <div data-sly-unwrap="$DOLLAR{}"></div>
    """)

    val fix = myFixture.quickFix<RemoveRedundantDataSlyUnwrapValueAction>("test.html")

    Assertions.assertThat(fix).isNull()
  }

  fun `test applying action for data-sly-unwrap with identifier`() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap.variable="$DOLLAR{}"></div>
    """)

    val fix by notNull<RemoveRedundantDataSlyUnwrapValueAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div data-sly-unwrap.variable></div>
    """)
  }

  fun `test applying action for data-sly-unwrap without identifier`() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap="$DOLLAR{}"></div>
    """)

    val fix by notNull<RemoveRedundantDataSlyUnwrapValueAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div data-sly-unwrap></div>
    """)
  }
}
