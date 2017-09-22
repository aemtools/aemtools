package com.aemtools.reference.html

import com.aemtools.reference.html.provider.DataSlyUseJavaReferenceProvider
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Register `com.intellij.modules.java` dependent html reference providers.
 *
 * @author Dmytro Troynikov
 */
class HtmlToJavaReferenceContributor : PsiReferenceContributor() {

  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    registrar.registerReferenceProvider(
        XmlPatterns.xmlAttributeValue(),
        DataSlyUseJavaReferenceProvider)

  }

}
