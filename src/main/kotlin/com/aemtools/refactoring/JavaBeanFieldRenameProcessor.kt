package com.aemtools.refactoring

import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.reference.common.reference.HtlPropertyAccessReference
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo

/**
 * Handler of rename of Java methods and fields.
 *
 * @author Dmytro Troynikov
 */
class JavaBeanFieldRenameProcessor : RenamePsiElementProcessor() {
    override fun canProcessElement(element: PsiElement): Boolean = element is PsiMethod

    override fun renameElement(element: PsiElement, newName: String, usages: Array<out UsageInfo>?, listener: RefactoringElementListener?) {
        usages?.forEach {
            println(it)
        }

        val propertyAccessReferences = usages?.map {
            it.reference as? HtlPropertyAccessReference
        }?.filterNotNull() ?: return super.renameElement(element, newName, usages, listener)

        propertyAccessReferences.forEach {
            val _actual = it.actualElement
            if (_actual is AccessIdentifierMixin) {
                _actual.setName(newName)
            }
        }

        //super.renameElement(element, newName, usages, listener)
    }


    override fun isInplaceRenameSupported(): Boolean = true
}
