package com.aemtools.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Some common IDEA Open API methods
 * @author Dmytro_Troynikov
 */
object OpenApiUtil {

    /**
     * Checks if given element is withing selection. Should be invoked from **Dispatch Thread**.
     */
    fun isCurrentElementSelected(element: PsiElement): Boolean {

        val editor: Editor = FileEditorManager
                .getInstance(element.project)
                .selectedTextEditor
                ?: return false
        val selectionModel = editor.selectionModel
        if (!selectionModel.hasSelection()) {
            return false
        }
        val selectedText = selectionModel.selectedText

        return element.text.contains(selectedText as CharSequence)
    }

    fun isCurrentThreadIsDispatch(): Boolean
            = ApplicationManager.getApplication().isDispatchThread

    fun iAmTest(): Boolean = ApplicationManager.getApplication().isUnitTestMode

}

/**
 * Execute given lambda as write command.
 *
 * @param project the project
 * @see WriteCommandAction
 * @see WriteCommandAction.Simple
 */
fun writeCommand(project: Project, lambda: () -> Unit): Unit {
    object : WriteCommandAction.Simple<Any>(project) {
        override fun run() {
            lambda.invoke()
        }

    }.execute().resultObject
}