package com.aemtools.reference.html

import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Html reference contributor.
 *
 * @author Dmytro Troynikov
 */
class HtmlReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttribute(),
                HtmlAttributeReferenceProvider)
        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttribute(),
                DataSlyUseWithinAttributeValueReferenceProvider)
    }

}