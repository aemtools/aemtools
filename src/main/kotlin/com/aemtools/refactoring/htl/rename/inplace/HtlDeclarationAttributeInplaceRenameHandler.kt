package com.aemtools.refactoring.htl.rename.inplace

import com.aemtools.completion.util.isHtlLocalDeclarationAttribute
import com.intellij.openapi.actionSystem.DataContext
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

    override fun invoke(project: com.intellij.openapi.project.Project, editor: com.intellij.openapi.editor.Editor?, file: com.intellij.psi.PsiFile?, dataContext: com.intellij.openapi.actionSystem.DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invoke(project: com.intellij.openapi.project.Project, elements: Array<out com.intellij.psi.PsiElement>, dataContext: com.intellij.openapi.actionSystem.DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}