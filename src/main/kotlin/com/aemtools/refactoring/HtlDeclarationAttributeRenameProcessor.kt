package com.aemtools.refactoring

import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.reference.htl.provider.HtlPropertyAccessReferenceProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeRenameProcessor : RenamePsiElementProcessor() {

    override fun canProcessElement(element: PsiElement): Boolean {
        return element is XmlAttribute
                && element.isHtlDeclarationAttribute()
    }

    override fun renameElement(element: PsiElement?, newName: String?, usages: Array<out UsageInfo>?, listener: RefactoringElementListener?) {
        if (element == null || newName == null) {
            return
        }

        val attribute = element as? XmlAttribute
                ?: return

        val htlAttributeName = attribute.htlAttributeName() ?: return

        val newAttributeName = "$htlAttributeName.$newName"
        attribute.setName(newAttributeName)

        usages?.filter { it.reference is HtlPropertyAccessReferenceProvider.HtlDeclarationReference }
                ?.filterNotNull()
                ?.forEach {
                    if (htlAttributeName in listOf(DATA_SLY_LIST, DATA_SLY_REPEAT)) {
                        if (it.element?.text?.endsWith("List") ?: false) {
                            it.reference?.handleElementRename("${newName}List")
                        } else {
                            it.reference?.handleElementRename(newName)
                        }
                    } else {
                        it.reference?.handleElementRename(newName)
                    }
                }
        listener?.elementRenamed(attribute)
    }

}