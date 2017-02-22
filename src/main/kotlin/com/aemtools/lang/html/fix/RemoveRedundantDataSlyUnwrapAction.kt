package com.aemtools.lang.html.fix

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

/**
 * @author Dmytro Troynikov
 */
class RemoveRedundantDataSlyUnwrapAction : IntentionAction {
    override fun getFamilyName(): String = "HTL Intentions"

    override fun startInWriteAction(): Boolean = true

    override fun getText(): String = "Remove attribute."

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val currentElement = file.findElementAt(editor.caretModel.offset)
                ?: return
        val document = PsiDocumentManager.getInstance(project).getDocument(file)
                ?: return

        val (start, end) = currentElement.textRange.startOffset to currentElement.textRange.endOffset
        document.replaceString(start, end, "")
    }
}