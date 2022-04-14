package com.aemtools.lang.html.annotator

import com.aemtools.common.util.writeCommand
import com.aemtools.test.base.BaseLightTest
import com.intellij.testFramework.InspectionFixtureTestCase

/**
 * @author Dmytro Primshyts
 */
class HtlAttributesAnnotatorRemoveUnusedVariableFixTest : BaseLightTest() {

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
