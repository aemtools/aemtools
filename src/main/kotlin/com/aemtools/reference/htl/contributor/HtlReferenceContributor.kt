package com.aemtools.reference.htl.contributor

import com.aemtools.lang.htl.psi.HtlPropertyAccess
import com.aemtools.lang.htl.psi.HtlTypes
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.resourceTypeOptionAssignment
import com.aemtools.reference.htl.provider.DataSlyCallPropertiesReferenceProvider
import com.aemtools.reference.htl.provider.DataSlyIncludeElReferenceProvider
import com.aemtools.reference.htl.provider.DataSlyUseElReferenceProvider
import com.aemtools.reference.htl.provider.HtlPropertyAccessReferenceProvider
import com.aemtools.reference.htl.provider.ResourceTypeReferenceProvider
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Htl reference contributor.
 *
 * @author Dmytro_Troynikov
 */
class HtlReferenceContributor : PsiReferenceContributor() {
  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    registrar.registerReferenceProvider(
        psiElement(HtlPropertyAccess::class.java),
        HtlPropertyAccessReferenceProvider)

    registrar.registerReferenceProvider(
        dataSlyUseMainString,
        DataSlyUseElReferenceProvider)

    registrar.registerReferenceProvider(
        dataSlyIncludeMainString,
        DataSlyIncludeElReferenceProvider)

    registrar.registerReferenceProvider(
        resourceTypeOptionAssignment,
        ResourceTypeReferenceProvider)

    registrar.registerReferenceProvider(
        psiElement(HtlTypes.VARIABLE_NAME),
        DataSlyCallPropertiesReferenceProvider)
  }
}
