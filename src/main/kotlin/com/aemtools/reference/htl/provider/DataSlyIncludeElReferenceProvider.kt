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
 * @author Dmytro_Troynikov
 */
object DataSlyIncludeElReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val value = element as HtlStringLiteralMixin
        val psiFile = HtlIndexFacade.resolveIncludeFile(value.name, value.containingFile)

        return if (psiFile != null) {
            arrayOf(PsiFileReference(psiFile, value, TextRange(1, value.textLength - 1)))
        } else {
            arrayOf()
        }
    }
}