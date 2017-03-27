package com.aemtools.reference.htl.contributor

import com.aemtools.lang.htl.psi.HtlPropertyAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseMainString
import com.aemtools.reference.htl.provider.DataSlyIncludeElReferenceProvider
import com.aemtools.reference.htl.provider.DataSlyUseElReferenceProvider
import com.aemtools.reference.htl.provider.HtlPropertyAccessReferenceProvider
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Htl reference contributor.
 * @author Dmytro_Troynikov
 */
class HtlReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(psiElement(HtlPropertyAccess::class.java),
                HtlPropertyAccessReferenceProvider)
        registrar.registerReferenceProvider(
                dataSlyUseMainString,
                DataSlyUseElReferenceProvider)

        registrar.registerReferenceProvider(
                dataSlyIncludeMainString,
                DataSlyIncludeElReferenceProvider
        )
    }
}