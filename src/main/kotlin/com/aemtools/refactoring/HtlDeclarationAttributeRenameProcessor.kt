package com.aemtools.refactoring

import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.aemtools.reference.htl.provider.HtlPropertyAccessReferenceProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeRenameProcessor : RenamePsiElementProcessor() {

    override fun canProcessElement(element: PsiElement): Boolean {
        return element is HtlPropertyAccessReferenceProvider.HtlDeclarationIdentifier
                && element.xmlAttribute.isHtlDeclarationAttribute()
    }

    override fun renameElement(element: PsiElement?, newName: String?, usages: Array<out UsageInfo>?, listener: RefactoringElementListener?) {
        if (element == null || newName == null) {
            return
        }

        super.renameElement(element, newName, usages, listener)
    }

//    override fun findReferences(element: PsiElement?): MutableCollection<PsiReference> {
//        return (element as? HtlPropertyAccessReferenceProvider.HtlDeclarationIdentifier)
//                ?.xmlAttribute
//                ?.references
//                ?.filter { it is HtlPropertyAccessReferenceProvider.HtlDeclarationReference }
//                ?.toMutableList()
//
//                ?: mutableListOf()
//    }

    override fun findReferences(element: PsiElement?, searchInCommentsAndStrings: Boolean): MutableCollection<PsiReference> {
        val originalElement = (element as? HtlPropertyAccessReferenceProvider.HtlDeclarationIdentifier)
                ?.xmlAttribute
        return super.findReferences(originalElement, searchInCommentsAndStrings)
    }

}