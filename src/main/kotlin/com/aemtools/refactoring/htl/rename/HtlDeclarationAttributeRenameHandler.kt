package com.aemtools.refactoring.htl.rename

import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.diagnostic.Logger
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

        return attribute.isHtlDeclarationAttribute()
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}