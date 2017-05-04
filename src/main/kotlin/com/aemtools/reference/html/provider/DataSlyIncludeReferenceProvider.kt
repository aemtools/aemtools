package com.aemtools.reference.html.provider

import com.aemtools.index.HtlIndexFacade
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object DataSlyIncludeReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val value = element as XmlAttributeValue

        val psiFile = HtlIndexFacade.resolveFile(value.value, value.containingFile)

        return if(psiFile != null) {
            arrayOf(PsiFileReference(psiFile, value, TextRange(1, value.textLength - 1)))
        } else {
            arrayOf()
        }
    }
}