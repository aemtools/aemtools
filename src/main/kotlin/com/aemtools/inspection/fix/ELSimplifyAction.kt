package com.aemtools.inspection.fix

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * @author Dmytro Troynikov
 */
class ELSimplifyAction(val element: HtlHtlEl) : LocalQuickFixOnPsiElement(element) {
  override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
    val newValue = element.findChildrenByType(HtlStringLiteralMixin::class.java)
        .firstOrNull()?.name ?: return
    val (start, end) = element.textRange.startOffset to element.textRange.endOffset

    val document = PsiDocumentManager.getInstance(project).getDocument(file)
        ?: return
    document.replaceString(start, end, newValue)
  }

  override fun getFamilyName(): String = "HTL Intentions"

  override fun getText(): String = "Simplify expression"
}
