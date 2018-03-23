package com.aemtools.reference.clientlib

import com.aemtools.lang.clientlib.psi.pattern.CdPatterns
import com.aemtools.reference.clientlib.provider.CdImportReferenceProvider
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * @author Dmytro_Troynikov
 */
class CdReferenceContributor : PsiReferenceContributor() {

  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    registrar.registerReferenceProvider(CdPatterns.include, CdImportReferenceProvider)
  }

}
