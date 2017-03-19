package com.aemtools.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
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