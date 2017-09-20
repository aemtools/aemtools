package com.aemtools.refactoring.htl.rename.inplace

import com.aemtools.completion.util.isHtlLocalDeclarationAttribute
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.intellij.refactoring.rename.RenameHandler

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeInplaceRenameHandler : RenameHandler {
  override fun isRenaming(dataContext: DataContext?): Boolean {
    return isAvailableOnDataContext(dataContext)
  }

  override fun isAvailableOnDataContext(dataContext: DataContext?): Boolean {
    if (dataContext == null) {
      return false
    }

    val element = PsiElementRenameHandler.getElement(dataContext)
        as? XmlAttribute
        ?: return false

    return element.isHtlLocalDeclarationAttribute()
  }

  override fun invoke(project: Project,
                      editor: Editor?,
                      file: PsiFile?,
                      dataContext: DataContext?) {
    TODO("not implemented")
  }

  override fun invoke(project: Project,
                      elements: Array<out PsiElement>,
                      dataContext: DataContext?) {
    TODO("not implemented")
  }
}
