package com.aemtools.reference.htl.provider

import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object DataSlyUseElReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
    val literal = element as? HtlStringLiteralMixin ?: return emptyArray()

    val psiFile = HtlIndexFacade.resolveUseFile(literal.name,
        element.containingFile)
        ?: return emptyArray()

    return arrayOf(
        PsiFileReference(psiFile,
            literal,
            TextRange(1, literal.textLength - 1))
    )
  }

}
