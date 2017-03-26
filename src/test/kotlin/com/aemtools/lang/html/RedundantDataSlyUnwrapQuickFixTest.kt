package com.aemtools.lang.html

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
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

    fun writeCommand(project: Project, code: () -> Unit): Unit {
        object : WriteCommandAction.Simple<Any>(project) {
            override fun run() {
                code.invoke()
            }

        }.execute().resultObject
    }

}
