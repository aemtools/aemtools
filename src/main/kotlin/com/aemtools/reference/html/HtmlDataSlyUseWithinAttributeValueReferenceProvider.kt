package com.aemtools.reference.html

import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isDataSlyUse
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.java.JavaSearch
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references in data-sly-use attributes.
 *
 * @author Dmytro_Troynikov
 */
object DataSlyUseWithinAttributeValueReferenceProvider : JavaClassReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val attr = element.findParentByType(XmlAttribute::class.java) ?: return arrayOf()
        val valueElement = attr.valueElement ?: return arrayOf()
        if (attr.isDataSlyUse()) {
            val psiFile = HtlIndexFacade.resolveSlyFile(valueElement.value, attr.containingFile)

            if (psiFile != null) {
                val fileReference = FileReference(psiFile,
                        valueElement,
                        TextRange(1, valueElement.textLength - 1))
                return arrayOf(fileReference)
            }

            val psiClass = JavaSearch.findClass(valueElement.value, element.project)
                    ?: return arrayOf()

            return getReferencesByString(psiClass.qualifiedName, element, 1)
        }
        return arrayOf()
    }

    class FileReference(val psiFile: PsiElement?,
                        holder: PsiElement,
                        range: TextRange) : PsiReferenceBase<PsiElement>(holder, range, true),
            PsiFileReference {

        override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {

            return arrayOf(object : ResolveResult {
                override fun getElement(): PsiElement? {
                    return psiFile
                }

                override fun isValidResult(): Boolean {
                    return true
                }

            })
        }

        override fun resolve(): PsiElement? {
            return psiFile
        }

        override fun getVariants(): Array<out Any> {
            return arrayOf()
        }

    }

}