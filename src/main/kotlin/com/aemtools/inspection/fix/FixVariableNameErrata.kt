package com.aemtools.inspection.fix

import com.aemtools.util.psiDocumentManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * Provides fix for variable that may contain errata.
 *
 * @author Dmytro Troynikov
 */
class FixVariableNameErrata(
    private val possiblyCorrectName: String,
    val element: PsiElement
) : BaseHtlFix(
    text = { "Change to '$possiblyCorrectName'" }
) {

  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val document = project.psiDocumentManager().getDocument(file)
        ?: return

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset

    document.replaceString(start, end, possiblyCorrectName)
    project.psiDocumentManager().commitDocument(document)
  }

}
