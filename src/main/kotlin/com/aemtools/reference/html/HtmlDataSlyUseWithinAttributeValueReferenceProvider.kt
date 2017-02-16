package com.aemtools.reference.html

import com.aemtools.completion.util.isDataSlyUse
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.java.JavaSearch
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.*
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references in data-sly-use attributes.
 *
 * @author Dmytro_Troynikov
 */
object DataSlyUseWithinAttributeValueReferenceProvider : JavaClassReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val attr = element as XmlAttribute
        val valueElement = attr.valueElement ?: return arrayOf()
        if (attr.isDataSlyUse()) {
            val psiFile = HtlIndexFacade.resolveSlyFile(valueElement.value, attr.containingFile)

            if (psiFile != null) {
                val fileReference = FileReference(psiFile, attr, TextRange(valueElement.startOffsetInParent+1, valueElement.startOffsetInParent + valueElement.textLength -2))
                return arrayOf(fileReference)
            }

            val psiClass = JavaSearch.findClass(valueElement.value, element.project)
                    ?: return arrayOf()

            return getReferencesByString(psiClass.qualifiedName, element, valueElement.startOffsetInParent + 1)
        }
        return arrayOf()
    }

    class FileReference(val psiFile: PsiFile,
                        holder: PsiElement,
                        range: TextRange) : PsiReferenceBase<PsiElement>(holder, range, true) {
        override fun resolve(): PsiElement? {
            return psiFile
        }

        override fun getVariants(): Array<out Any> {
            return arrayOf()
        }

    }

}