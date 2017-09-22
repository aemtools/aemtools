package com.aemtools.lang.htl.annotator

import com.aemtools.blocks.util.quickFix
import com.aemtools.constant.const.DOLLAR
import com.aemtools.inspection.fix.FixVariableNameErrata
import com.aemtools.util.writeCommand
import com.intellij.testFramework.InspectionFixtureTestCase

/**
 * @author Dmytro Troynikov
 */
class FixVariableNameErrataTest : InspectionFixtureTestCase() {

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
