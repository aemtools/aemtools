package com.aemtools.refactoring.htl.rename

import com.aemtools.reference.htl.reference.HtlDeclarationReference
import com.aemtools.reference.htl.reference.HtlListHelperReference
import com.intellij.codeInsight.TargetElementEvaluator
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

/**
 * @author Dmytro Troynikov
 */
class HtlAttributeTargetElementEvaluator : TargetElementEvaluator {

  override fun getElementByReference(ref: PsiReference, flags: Int): PsiElement? {
    return when (ref) {
      is HtlDeclarationReference -> ref.xmlAttribute
      is HtlListHelperReference -> ref.xmlAttribute
      else -> null
    }
  }

  override fun includeSelfInGotoImplementation(element: PsiElement): Boolean = true

}
