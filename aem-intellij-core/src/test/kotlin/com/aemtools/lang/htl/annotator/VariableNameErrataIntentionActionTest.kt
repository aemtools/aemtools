package com.aemtools.lang.htl.annotator

import com.aemtools.common.util.writeCommand
import com.aemtools.inspection.fix.VariableNameErrataIntentionAction
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [VariableNameErrataIntentionAction].
 *
 * @author Dmytro Primshyts
 */
class VariableNameErrataIntentionActionTest : BaseLightTest() {

  fun testFixErrataCorrectFormat() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.myModel=""></div>
            $DOLLAR{mymodel}
        """.trimIndent())

    val fix by notNull<VariableNameErrataIntentionAction> {
      myFixture.quickFix("test.html")
    }

    assertThat(fix.familyName)
        .isEqualTo("HTL Intentions")

    assertThat(fix.startInWriteAction())
        .isTrue()

    assertThat(fix.text)
        .isEqualTo("Change to 'myModel'")

    assertThat(fix.isAvailable(project, editor, null))
        .isTrue()
  }

  fun testFixErrata() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.myModel=""></div>
            $DOLLAR{mymodel}
        """.trimIndent())

    val fix: VariableNameErrataIntentionAction = myFixture.quickFix("test.html")
        ?: throw AssertionError("No quick fix found!")

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <div data-sly-use.myModel=""></div>
            $DOLLAR{myModel}
        """.trimIndent())
  }

  fun testFixErrataInContextObject() {
    myFixture.configureByText("test.html", "$DOLLAR{propert1es}")

    val fix: VariableNameErrataIntentionAction = myFixture.quickFix("test.html")
        ?: throw AssertionError("No quick fix found!")

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("$DOLLAR{properties}")
  }

}
