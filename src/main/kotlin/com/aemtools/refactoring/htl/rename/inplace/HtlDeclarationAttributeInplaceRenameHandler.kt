package com.aemtools.refactoring.htl.rename.inplace

import com.intellij.refactoring.rename.RenameHandler

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeInplaceRenameHandler : RenameHandler {
    override fun isRenaming(dataContext: com.intellij.openapi.actionSystem.DataContext?): Boolean {
        if (dataContext == null) {
            return false
        }

        return false
    }

    override fun isAvailableOnDataContext(dataContext: com.intellij.openapi.actionSystem.DataContext?): Boolean {
        if (dataContext == null) {
            return false
        }

        val element = com.intellij.refactoring.rename.PsiElementRenameHandler.getElement(dataContext)
        return false
    }

    override fun invoke(project: com.intellij.openapi.project.Project, editor: com.intellij.openapi.editor.Editor?, file: com.intellij.psi.PsiFile?, dataContext: com.intellij.openapi.actionSystem.DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invoke(project: com.intellij.openapi.project.Project, elements: Array<out com.intellij.psi.PsiElement>, dataContext: com.intellij.openapi.actionSystem.DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}