package com.aemtools.codeinsight.htl.intention

import com.aemtools.codeinsight.htl.annotator.swapQuotes
import com.aemtools.common.intention.BaseHtlIntentionAction
import com.aemtools.common.util.psiDocumentManager
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer

/**
 * Inverts [HtlStringLiteralMixin] quotes.
 *
 * @author Dmytro Primshyts
 */
class HtlWrongQuotesLiteralFixIntentionAction(
    private val pointer: SmartPsiElementPointer<HtlStringLiteralMixin>
) : BaseHtlIntentionAction(
    text = { "Invert HTL Literal quotes" }
) {

  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return
    val document = project.psiDocumentManager().getDocument(file)
        ?: return

    val newValue = element.swapQuotes()

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset

    document.replaceString(start, end, newValue)
    project.psiDocumentManager().commitDocument(document)
  }

}
