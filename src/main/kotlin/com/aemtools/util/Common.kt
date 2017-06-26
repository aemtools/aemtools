package com.aemtools.util

import com.aemtools.constant.const.htl.DECLARATION_ATTRIBUTES
import com.aemtools.constant.const.htl.SINGLE_ATTRIBUTES
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.impl.local.LocalFileSystemBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.refactoring.RefactoringBundle
import com.intellij.refactoring.util.CommonRefactoringUtil

/**
 * Some common IDEA Open API methods
 *
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

    /**
     * Check if current IDEA process is executed within a test.
     *
     * @return *true* is current app is running in test
     */
    fun iAmTest(): Boolean = ApplicationManager.getApplication().isUnitTestMode

    /**
     * Find [VirtualFile] by full file path.
     *
     * @param path path to file
     * @return instance of VirtualFile, *null* if no file was found by given path
     */
    fun findFileByPath(path: String): VirtualFile? {
        return LocalFileSystemBase.getInstance().findFileByPath(path)
    }

    /**
     * Find [VirtualFile] by project relative path.
     *
     * @param project the project
     * @param relativePath the path
     * @return instance of VirtualFile, *null* if no file was found by given path
     */
    fun findFileByRelativePath(relativePath: String, project: Project): VirtualFile? {
        val files = FilenameIndex.getFilesByName(
                project,
                relativePath.substringAfterLast("/"),
                GlobalSearchScope.projectScope(project))
        return files.find { it.virtualFile.path.endsWith(relativePath) }
                ?.virtualFile
    }

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

/**
 * Check if current string is valid htl attribute name.
 *
 * @receiver [String]
 * @return *true* if current string is valid htl attribute name, *false* otherwise
 */
fun String.isHtlAttributeName(): Boolean = when (this.substringBefore(".")) {
    in DECLARATION_ATTRIBUTES -> {
        DECLARATION_ATTRIBUTES.any { it == this || this.startsWith("$it.") }
    }
    in SINGLE_ATTRIBUTES -> {
        SINGLE_ATTRIBUTES.any { it == this }
    }
    else -> false
}

/**
 * Add priority to current [LookupElement].
 *
 * @param priority the priority
 *
 * @receiver [LookupElement]
 * @return [PrioritizedLookupElement] with given priority
 */
fun LookupElement.withPriority(priority: Double): LookupElement =
        PrioritizedLookupElement.withPriority(this, priority)

/**
 * Add proximity to current [LookupElement].
 *
 * @param proximity the proximity
 * @receiver [LookupElement]
 * @return [PrioritizedLookupElement] with given proximity
 */
fun LookupElement.withProximity(proximity: Int) =
        PrioritizedLookupElement.withExplicitProximity(this, proximity)

/**
 * Convert current [String] to [StringBuilder].
 *
 * @receiver [String]
 * @return new string builder instance that contains current string
 */
fun String.toStringBuilder() = StringBuilder(this)

/**
 * Get [PsiFileFactory] associated with current project.
 *
 * @receiver [Project]
 * @return instance of psi file factory
 */
fun Project.psiFileFactory(): PsiFileFactory = PsiFileFactory.getInstance(this)

/**
 * Get [PsiManager] associated with current project.
 *
 * @receiver [Project]
 * @return instance of psi manager
 */
fun Project.psiManager(): PsiManager = PsiManager.getInstance(this)

/**
 * Show error message popup.
 *
 * @param project the project
 * @param editor the editor
 * @param message the message
 */
fun showErrorMessage(project: Project, editor: Editor?, message: String) {
    CommonRefactoringUtil.showErrorHint(
            project,
            editor,
            message,
            RefactoringBundle.message("rename.title"),
            null
    )
}