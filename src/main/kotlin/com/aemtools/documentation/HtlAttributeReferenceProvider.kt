package com.aemtools.documentation

import com.aemtools.completion.util.isDataSlyUse
import com.aemtools.completion.util.isHtlAttribute
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.XmlAttributeImpl
import com.intellij.psi.impl.source.xml.XmlAttributeReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references for Htl attributes to make it possible to trigger `Quick Documentation` action on them.
 *
 * @author Dmytro Troynikov
 */
open class ReferenceContributor : PsiReferenceContributor() {

    private val referenceProvider = HtlAttributeReferenceProvider()

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttribute(),
                referenceProvider)
        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttribute(),
                DataSlyUseWithinAttributeValueReferenceProvider())
    }

}

class DataSlyUseWithinAttributeValueReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val attr = element as XmlAttribute
        val valueElement = attr.valueElement ?: return arrayOf()
        if (attr.isDataSlyUse()) {
            val jpf = JavaPsiFacade.getInstance((element.project))

            val psiClass = jpf.findClass(valueElement.value,
                    GlobalSearchScope.allScope(element.project))
            // TODO: create reference to java/kotlin class
            return psiClass?.references ?: arrayOf()
        }
        return arrayOf()
    }

}

class HtlAttributeReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement,
                                        context: ProcessingContext): Array<out PsiReference> {
        val attr = element as XmlAttribute

        if (attr.isHtlAttribute()) {
            return arrayOf(XmlAttributeReferenceWrapper(attr as XmlAttributeImpl))
        }

        return arrayOf()
    }
}

class XmlAttributeReferenceWrapper(val xmlAttribute: XmlAttributeImpl) : XmlAttributeReference(xmlAttribute) {
    override fun resolve() = xmlAttribute
}