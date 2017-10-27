package com.aemtools.lang.html.annotator

import com.aemtools.util.writeCommand
import com.intellij.testFramework.InspectionFixtureTestCase

/**
 * @author Dmytro Troynikov
 */
class HtlAttributesAnnotatorRemoveUnusedVariableFixTest : InspectionFixtureTestCase() {

  fun testQuickFixWithDataSlyUse() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.bean=""></div>
        """)

    val fix = myFixture.getAllQuickFixes("test.html")
        .first()

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <div data-sly-use=""></div>
        """)
  }

  fun testQuickFixWithDataSlyTest() {
    myFixture.configureByText("test.html", """
            <div data-sly-test.condition=""></div>
        """)

    val fix = myFixture.getAllQuickFixes("test.html")
        .first()

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <div data-sly-test=""></div>
        """)
  }

}
