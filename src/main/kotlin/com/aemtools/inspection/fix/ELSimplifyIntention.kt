package com.aemtools.inspection.fix

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer

/**
 * @author Dmytro Troynikov
 */
class ELSimplifyIntention(private val pointer: SmartPsiElementPointer<HtlHtlEl>)
  : BaseHtlFix(
    text = { "Simplify expression" }
) {

  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return

    val newValue = element.findChildrenByType(HtlStringLiteralMixin::class.java)
        .firstOrNull()?.name ?: return
    val (start, end) = element.textRange.startOffset to element.textRange.endOffset

    val document = PsiDocumentManager.getInstance(project).getDocument(file)
        ?: return
    document.replaceString(start, end, newValue)
  }

}
