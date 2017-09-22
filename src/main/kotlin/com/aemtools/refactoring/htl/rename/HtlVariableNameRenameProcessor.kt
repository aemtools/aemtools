package com.aemtools.refactoring.htl.rename

import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.psi.PsiElement
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo

/**
 * @author Dmytro Troynikov
 */
class HtlVariableNameRenameProcessor : RenamePsiElementProcessor() {

  override fun canProcessElement(element: PsiElement): Boolean {
    return element is HtlVariableName
  }

  override fun renameElement(
      element: PsiElement?,
      newName: String?,
      usages: Array<out UsageInfo>?,
      listener: RefactoringElementListener?) {
    if (element == null || newName == null) {
      return
    }

    val variableName = element as? VariableNameMixin
        ?: return

    variableName.setName(newName)
    usages?.forEach { it.reference?.handleElementRename(newName) }

    listener?.elementRenamed(variableName)
  }

}
