package com.aemtools.codeinsight.impicitusage

import com.aemtools.common.constant.Const.Sling.ACS_COMMONS_INJECTORS
import com.aemtools.common.constant.Const.Sling.JAVAX_INJECT
import com.aemtools.common.constant.Const.Sling.SLING_INJECTORS
import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMember

class SlingModelFieldsImplicitUsageProvider : ImplicitUsageProvider {
    override fun isImplicitWrite(element: PsiElement): Boolean {
        return when (element) {
            is PsiField -> annotatedWithFieldInjectorsAnnotation(element)
            else -> false
        }
    }

    override fun isImplicitUsage(element: PsiElement): Boolean {
        return isImplicitRead(element) || isImplicitWrite(element)
    }

    override fun isImplicitRead(element: PsiElement): Boolean {
        return false
    }

    private fun annotatedWithFieldInjectorsAnnotation(psiField: PsiMember): Boolean =
        FIELD_SLING_INJECTORS.any { psiField.hasAnnotation(it) }

    companion object {
        val FIELD_SLING_INJECTORS: List<String> = SLING_INJECTORS + ACS_COMMONS_INJECTORS + JAVAX_INJECT
    }
}
