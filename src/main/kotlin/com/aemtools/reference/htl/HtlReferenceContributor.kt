package com.aemtools.reference.htl

import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

/**
 * Htl reference contributor.
 * @author Dmytro_Troynikov
 */
class HtlReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {

        registrar.registerReferenceProvider(psiElement(HtlStringLiteral::class.java),
                HtlStringLiteralReferenceProvider)

    }
}