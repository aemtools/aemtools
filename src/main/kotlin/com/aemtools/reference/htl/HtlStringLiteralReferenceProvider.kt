package com.aemtools.reference.htl

import com.aemtools.completion.util.isInsideOf
import com.aemtools.completion.util.isMainString
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.java.JavaSearch
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * Reference provider for Htl string literals.
 * @author Dmytro_Troynikov
 */
object HtlStringLiteralReferenceProvider : JavaClassReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val literal = element as? HtlStringLiteral ?: return arrayOf()

        //TODO: add reference for ${@ context=''}
        if (literal.isMainString()
                && literal.isInsideOf(DATA_SLY_USE)) {
            val myText = literal.text.substring(1, literal.text.length - 1)
            val psiClass = JavaSearch.findClass(myText, literal.project) ?: return arrayOf()

            return getReferencesByString(psiClass.qualifiedName, literal, 1)
        }
        return arrayOf()
    }

}