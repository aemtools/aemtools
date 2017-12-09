package com.aemtools.inspection.java.fix

import com.aemtools.common.intention.BaseAemIntellijIntentionAction
import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.SmartPsiElementPointer

/**
 * Replaces given [PsiLiteralExpression] with full qualified usage.
 *
 * @author Dmytro Troynikov
 */
class ReplaceHardcodedLiteralWithFqnAction(
    message: String,
    val cd: ConstantDescriptor,
    val psiLiteral: SmartPsiElementPointer<PsiLiteralExpression>
) : BaseAemIntellijIntentionAction(
    { message },
    "AEM Inspections"
) {
  override fun invoke(project: Project, editor: Editor?, file: PsiFile) {
    val element = psiLiteral.element ?: return

    val document = PsiDocumentManager.getInstance(project).getDocument(file)
        ?: return

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset
    document.replaceString(start, end, "${cd.containerClass}.${cd.name}")
    PsiDocumentManager.getInstance(project).commitDocument(document)
  }
}
