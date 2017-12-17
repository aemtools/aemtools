package com.aemtools.inspection.html.fix

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.psiDocumentManager
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer

/**
 * @author Dmytro Troynikov
 */
class RemoveRedundantElAction(private val pointer: SmartPsiElementPointer<HtlElExpressionMixin>)
  : BaseHtlIntentionAction(
    text = { "Remove redundant expression." }
) {

  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return

    val psiDocumentManager = project.psiDocumentManager()

    val document = psiDocumentManager.getDocument(file)
        ?: return

    val newValue = element.findChildrenByType(HtlStringLiteralMixin::class.java)
        .firstOrNull()?.name ?: return

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset

    document.replaceString(start, end, newValue)
    psiDocumentManager.commitDocument(document)
  }

}
