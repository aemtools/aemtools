package com.aemtools.refactoring.htl.rename

import com.aemtools.completion.util.isHtlGlobalDeclarationAttribute
import com.aemtools.refactoring.htl.rename.util.RenameUtil
import com.aemtools.refactoring.htl.rename.util.RenameUtil.getElement
import com.aemtools.refactoring.htl.rename.util.RenameUtil.rename
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.refactoring.actions.BaseRefactoringAction
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.intellij.refactoring.rename.PsiElementRenameHandler.DEFAULT_NAME
import com.intellij.refactoring.rename.RenameHandler

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeRenameHandler : RenameHandler {
  companion object {
    val LOG = Logger.getInstance("#com.aemtools.refactoring.htl.rename.HtlDeclarationAttributeRenameHandler")
  }

  override fun isRenaming(dataContext: DataContext?): Boolean {
    return isAvailableOnDataContext(dataContext)
  }

  override fun isAvailableOnDataContext(dataContext: DataContext?): Boolean {
    if (dataContext == null) {
      return false
    }

    val attribute = PsiElementRenameHandler.getElement(dataContext) as? XmlAttribute
        ?: return false

    return attribute.isHtlGlobalDeclarationAttribute()
  }

  override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
    val element = getElement(dataContext)
        ?: BaseRefactoringAction.getElementAtCaret(editor, file)
        ?: return
    if (dataContext == null || editor == null || file == null) {
      return
    }

    if (ApplicationManager.getApplication().isUnitTestMode) {
      val newName = DEFAULT_NAME.getData(dataContext)
      if (newName != null) {
        rename(element, project, element, editor, newName)
        return
      }
    }

    editor.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)
    val nameSuggestionContext = InjectedLanguageUtil.findElementAtNoCommit(file, editor.caretModel.offset)
    RenameUtil.invoke(element, project, nameSuggestionContext, editor)
  }

  override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
