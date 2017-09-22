package com.aemtools.lang.html

import com.aemtools.util.writeCommand
import com.intellij.testFramework.InspectionFixtureTestCase

/**
 * @author Dmytro_Troynikov
 */
class RedundantDataSlyUnwrapQuickFixTest : InspectionFixtureTestCase() {

  fun testQuickFix() {
    myFixture.configureByText("test.html", """
            <sly data-sly-unwrap></sly>
        """.trimIndent())
    val fix = myFixture.getAllQuickFixes("test.html")
        .first()

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
            <sly ></sly>
        """.trimIndent())
  }

}
