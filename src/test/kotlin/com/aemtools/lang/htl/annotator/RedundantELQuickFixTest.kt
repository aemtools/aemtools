package com.aemtools.lang.htl.annotator

import com.aemtools.constant.const.DOLLAR
import com.aemtools.util.writeCommand
import com.intellij.testFramework.InspectionFixtureTestCase

/**
 * @author Dmytro Troynikov
 */
class RedundantELQuickFixTest : InspectionFixtureTestCase() {

  fun testRedundantELQuickFixForDataSlyUse() {
    myFixture.configureByText("test.html", """
            <div data-sly-use="$DOLLAR{'com.test.Bean'}"></div>
        """.trimIndent())

    val fix = myFixture.getAllQuickFixes("test.html")
        .first()

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <div data-sly-use="com.test.Bean"></div>
        """.trimIndent())
  }

  fun testRedundantELQuickFixForDataSlyUse1() {
    myFixture.configureByText("test.html", """
            <div data-sly-use="$DOLLAR{'com.test.Bean'}"></div>
        """.trimIndent())

    val fix = myFixture.getAllQuickFixes("test.html")
        .first()

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <div data-sly-use="com.test.Bean"></div>
        """.trimIndent())
  }

  fun testRedundantELQuickFixForDataSlyInclude() {
    myFixture.configureByText("test.html", """
            <div data-sly-include="$DOLLAR{'template.html'}"></div>
        """.trimIndent())

    val fix = myFixture.getAllQuickFixes("test.html")
        .first()

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <div data-sly-include="template.html"></div>
        """.trimIndent())
  }

}
