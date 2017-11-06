package com.aemtools.lang.htl.annotator

import com.aemtools.blocks.util.notNull
import com.aemtools.blocks.util.quickFix
import com.aemtools.constant.const.DOLLAR
import com.aemtools.inspection.fix.SimplifyElIntentionAction
import com.aemtools.util.writeCommand
import com.intellij.testFramework.InspectionFixtureTestCase
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [SimplifyElIntentionAction].
 *
 * @author Dmytro Troynikov
 */
class RedundantELQuickFixTest : InspectionFixtureTestCase() {

  fun testRedundantELCorrectFormat() {
    myFixture.configureByText("test.html", """
            <div data-sly-use="$DOLLAR{'com.test.Bean'}"></div>
        """.trimIndent())

    val fix by notNull<SimplifyElIntentionAction> {
      myFixture.quickFix("test.html")
    }

    assertThat(fix.familyName)
        .isEqualTo("HTL Intentions")

    assertThat(fix.startInWriteAction())
        .isTrue()

    assertThat(fix.text)
        .isEqualTo("Simplify expression")
  }

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
