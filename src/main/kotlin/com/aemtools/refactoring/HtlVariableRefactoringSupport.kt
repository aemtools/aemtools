package com.aemtools.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class HtlVariableRefactoringSupport : RefactoringSupportProvider() {
  override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
    return false
  }
}
