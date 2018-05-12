package com.aemtools.common.patterns

import com.aemtools.common.util.injectedLanguageManager
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
class HostCondition(
    message: String,
    private val condition:
    (PsiElement,
     PsiLanguageInjectionHost,
     ProcessingContext?) -> Boolean) : PatternCondition<PsiElement>(message) {
  override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
    val host = t.project.injectedLanguageManager().getInjectionHost(t)
        ?: return false

    return condition(t, host, context)
  }
}
