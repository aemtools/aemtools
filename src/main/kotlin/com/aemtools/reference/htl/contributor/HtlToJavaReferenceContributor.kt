package com.aemtools.reference.htl.contributor

import com.aemtools.lang.htl.psi.pattern.HtlPatterns
import com.aemtools.reference.htl.provider.DataSlyUseElJavaReferenceProvider
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Register `com.intellij.modules.java` dependent htl reference providers.
 *
 * @author Dmytro Troynikov
 */
class HtlToJavaReferenceContributor : PsiReferenceContributor() {
  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    registrar.registerReferenceProvider(
        HtlPatterns.dataSlyUseMainString,
        DataSlyUseElJavaReferenceProvider)
  }

}
