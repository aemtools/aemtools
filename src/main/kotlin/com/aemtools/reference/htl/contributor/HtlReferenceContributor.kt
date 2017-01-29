package com.aemtools.reference.htl.contributor

import com.aemtools.lang.htl.psi.HtlPropertyAccess
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.reference.htl.provider.HtlPropertyAccessReferenceProvider
import com.aemtools.reference.htl.provider.HtlStringLiteralReferenceProvider
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
        registrar.registerReferenceProvider(psiElement(HtlStringLiteral::class.java),
                HtlStringLiteralReferenceProvider)
    }
}