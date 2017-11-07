package com.aemtools.lang.htl.annotator

import com.aemtools.constant.const.DOLLAR
import com.aemtools.inspection.fix.FixVariableNameErrata
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import com.aemtools.util.writeCommand
import com.intellij.testFramework.InspectionFixtureTestCase
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [FixVariableNameErrata].
 *
 * @author Dmytro Troynikov
 */
class FixVariableNameErrataTest : InspectionFixtureTestCase() {

  fun testFixErrataCorrectFormat() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.myModel=""></div>
            $DOLLAR{mymodel}
        """.trimIndent())

    val fix by notNull<FixVariableNameErrata> {
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

    val fix: FixVariableNameErrata = myFixture.quickFix("test.html")
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

    val fix: FixVariableNameErrata = myFixture.quickFix("test.html")
        ?: throw AssertionError("No quick fix found!")

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("$DOLLAR{properties}")
  }

}
