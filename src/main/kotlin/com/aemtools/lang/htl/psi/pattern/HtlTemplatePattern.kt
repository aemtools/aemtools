package com.aemtools.lang.htl.psi.pattern

import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isInsideOf
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * Pattern that matches htl expression inside of given htl attribute.
 *
 * @author Dmytro Troynikov
 */
class HtlTemplatePattern(val name: String) : PatternCondition<PsiElement?>(name) {

  override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
    return element.findParentByType(HtlHtlEl::class.java)
        ?.isInsideOf(name)
        ?: false
  }

}
