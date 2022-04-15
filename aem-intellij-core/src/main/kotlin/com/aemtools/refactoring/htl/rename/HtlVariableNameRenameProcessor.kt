package com.aemtools.refactoring.htl.rename

import com.aemtools.lang.htl.psi.HtlVariableName
import com.intellij.psi.PsiElement
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo

/**
 * @author Dmytro Primshyts
 */
class HtlVariableNameRenameProcessor : RenamePsiElementProcessor() {

  override fun canProcessElement(element: PsiElement): Boolean {
    return element is HtlVariableName
  }

  override fun renameElement(
      element: PsiElement,
      newName: String,
      usages: Array<out UsageInfo>,
      listener: RefactoringElementListener?) {
    val variableName = element as? com.aemtools.lang.htl.psi.mixin.VariableNameMixin
        ?: return

    variableName.setName(newName)
    usages.forEach { it.reference?.handleElementRename(newName) }

    listener?.elementRenamed(variableName)
  }

}
