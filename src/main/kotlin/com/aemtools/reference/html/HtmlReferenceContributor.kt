package com.aemtools.reference.html

import com.aemtools.reference.html.provider.DataSlyIncludeReferenceProvider
import com.aemtools.reference.html.provider.HtmlDataSlyUseWithinAttributeValueReferenceProvider
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
        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue(),
                HtmlDataSlyUseWithinAttributeValueReferenceProvider)

        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue()
                        .withAncestor(1, XmlPatterns.xmlAttribute("data-sly-include")),
                DataSlyIncludeReferenceProvider
        )
    }

}