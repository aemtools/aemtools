package com.aemtools.reference.htl

import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * Reference provider for Htl string literals.
 * @author Dmytro_Troynikov
 */
object HtlStringLiteralReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val literal = element as? HtlStringLiteral ?: return arrayOf()

        //TODO: add reference for ${@ context=''}
        //TODO: add reference for ${'bean name'}

        return arrayOf()
    }

}