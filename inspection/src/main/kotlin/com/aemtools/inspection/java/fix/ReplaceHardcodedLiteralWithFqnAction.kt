package com.aemtools.inspection.java.fix

import com.aemtools.common.intention.BaseAemIntellijIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
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
    val fqn: String,
    val psiLiteral: SmartPsiElementPointer<PsiLiteralExpression>
) : BaseAemIntellijIntentionAction(
        { message },
        "AEM Inspections"
    ) {
  override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    val element = psiLiteral.element ?: return


  }
}
