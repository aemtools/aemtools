package com.aemtools.reference.html

import com.aemtools.completion.util.isDataSlyUse
import com.aemtools.lang.java.JavaSearch
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references in data-sly-use attributes.
 *
 * @author Dmytro_Troynikov
 */
object DataSlyUseWithinAttributeValueReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val attr = element as XmlAttribute
        val valueElement = attr.valueElement ?: return arrayOf()
        if (attr.isDataSlyUse()) {
            val psiClass = JavaSearch.findClass(valueElement.value, element.project)

            // TODO: create reference to java/kotlin class
            return psiClass?.references ?: arrayOf()
        }
        return arrayOf()
    }

}