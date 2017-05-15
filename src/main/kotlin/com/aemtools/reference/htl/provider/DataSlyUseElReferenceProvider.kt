package com.aemtools.reference.htl.provider

import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.lang.java.JavaSearch
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * Reference provider for Htl string literals.
 * @author Dmytro_Troynikov
 */
object DataSlyUseElReferenceProvider : JavaClassReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val literal = element as? HtlStringLiteralMixin ?: return arrayOf()

        val psiFile = HtlIndexFacade.resolveFile(literal.name, element.containingFile)

        if (psiFile != null) {
            val fileReference = PsiFileReference(psiFile,
                    literal,
                    TextRange(1, literal.textLength - 1))
            return arrayOf(fileReference)
        }

        val psiClass = JavaSearch.findClass(literal.name, literal.project) ?: return arrayOf()

        return getReferencesByString(psiClass.qualifiedName, literal, 1)
    }

}