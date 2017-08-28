package com.aemtools.lang.htl.annotator

import com.aemtools.constant.const.DOLLAR
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

        val fix = myFixture.getAllQuickFixes("test.html")[1]

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

        val fix = myFixture.getAllQuickFixes("test.html")
                .first()

        writeCommand(project) {
            fix.invoke(project, editor, file)
        }

        myFixture.checkResult("$DOLLAR{properties}")
    }

}