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
        val valueElement = element as XmlAttributeValue
        val value = valueElement.value ?: return emptyArray()

        val psiFile = HtlIndexFacade.resolveIncludeFile(value, valueElement.containingFile)

        return if(psiFile != null) {
            arrayOf(PsiFileReference(psiFile, valueElement, TextRange(1, valueElement.textLength - 1)))
        } else {
            arrayOf()
        }
    }
}