package com.aemtools.completion.htl.model

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiClass

/**
 * The result of type resolution.
 * @author Dmytro_Troynikov
 */
data class ResolutionResult(val psiClass: PsiClass? = null,
                            val predefined: List<LookupElement>? = null) {

    /**
     * Check if current [ResolutionResult] is empty.
     */
    fun isEmpty() = psiClass == null && predefined == null

    /**
     * Check if current [ResolutionResult] is not empty.
     */
    fun isNotEmpty() = !isEmpty()

}