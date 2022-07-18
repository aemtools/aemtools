package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.util.writeCommand
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import org.assertj.core.api.Assertions

/**
 * Tests for [RemoveHtlIdentifierAction].
 *
 * @author Kostiantyn Diachenko
 */
class RemoveHtlIdentifierActionTest : BaseLightTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }

  fun `test action for data-sly-unwrap identifier`() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap.variable=""></div>
    """)

    val fix by notNull<RemoveHtlIdentifierAction> {
      myFixture.quickFix("test.html")
    }

    Assertions.assertThat(fix.familyName).isEqualTo("HTL Intentions")
    Assertions.assertThat(fix.startInWriteAction()).isTrue
    Assertions.assertThat(fix.text).isEqualTo("Remove \"variable\" identifier")
    Assertions.assertThat(fix.isAvailable(project, editor, null)).isTrue
  }

  fun `test absent action for data-sly-unwrap identifier in HTL 1_4`() {
    myFixture.setHtlVersion(HtlVersion.V_1_4)

    myFixture.configureByText("test.html", """
      <div data-sly-unwrap.variable=""></div>
    """)

    val quickFix = myFixture.quickFix<RemoveHtlIdentifierAction>("test.html")

    Assertions.assertThat(quickFix).isNull()
  }

  fun `test applying action for data-sly-unwrap identifier`() {
    myFixture.configureByText("test.html", """
      <div data-sly-unwrap.variable=""></div>
    """)

    val fix by notNull<RemoveHtlIdentifierAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div data-sly-unwrap=""></div>
    """)
  }
}
