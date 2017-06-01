package com.aemtools.refactoring

import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.intellij.psi.PsiElement
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo

/**
 * @author Dmytro Troynikov
 */
class HtlVariableRenameProcessor : RenamePsiElementProcessor() {
    override fun canProcessElement(element: PsiElement): Boolean {
        return element is AccessIdentifierMixin
    }

    override fun isInplaceRenameSupported(): Boolean {
        return true
    }

    override fun renameElement(element: PsiElement?, newName: String?, usages: Array<out UsageInfo>?, listener: RefactoringElementListener?) {
        if (element == null || newName == null || usages == null) {
            return
        }

        usages.first()
    }

}