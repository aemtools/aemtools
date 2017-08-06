package com.aemtools.findusages

import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.aemtools.reference.htl.reference.HtlDeclarationReference
import com.aemtools.reference.htl.reference.HtlListHelperReference
import com.intellij.find.findUsages.AbstractFindUsagesDialog
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.xml.XmlAttribute
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor

/**
 * @author Dmytro Troynikov
 */
class HtmlAttributeUsagesProvider : FindUsagesHandlerFactory() {
    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler? {
        return if (element is XmlAttribute && element.isHtlDeclarationAttribute()) {
            HtlAttributesFindUsagesHandler(element)
        } else {
            null
        }
    }

    override fun canFindUsages(element: PsiElement): Boolean {
        return element is XmlAttribute && element.isHtlDeclarationAttribute()
    }

    class HtlAttributesFindUsagesHandler(xmlAttribute: XmlAttribute) : FindUsagesHandler(xmlAttribute) {
        override fun isSearchForTextOccurrencesAvailable(psiElement: PsiElement, isSingleFile: Boolean): Boolean {
            return super.isSearchForTextOccurrencesAvailable(psiElement, isSingleFile)
        }

        override fun processElementUsages(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {
            return super.processElementUsages(element, processor, options)
        }

        override fun isSearchForTextOccurencesAvailable(psiElement: PsiElement, isSingleFile: Boolean): Boolean {
            return super.isSearchForTextOccurencesAvailable(psiElement, isSingleFile)
        }

        override fun findReferencesToHighlight(target: PsiElement, searchScope: SearchScope): MutableCollection<PsiReference> {
            return super.findReferencesToHighlight(target, searchScope)
                    .filter { it is HtlDeclarationReference || it is HtlListHelperReference }
                    .toMutableList()
        }

        override fun getStringsToSearch(element: PsiElement): MutableCollection<String> {
            return super.getStringsToSearch(element) ?: mutableListOf()
        }

        override fun getFindUsagesOptions(): FindUsagesOptions {
            return super.getFindUsagesOptions()
        }

        override fun getFindUsagesOptions(dataContext: DataContext?): FindUsagesOptions {
            return super.getFindUsagesOptions(dataContext)
        }

        override fun getPrimaryElements(): Array<PsiElement> {
            return super.getPrimaryElements()
        }

        override fun getHelpId(): String? {
            return super.getHelpId()
        }

        override fun getSecondaryElements(): Array<PsiElement> {
            return super.getSecondaryElements()
        }

        override fun getFindUsagesDialog(isSingleFile: Boolean, toShowInNewTab: Boolean, mustOpenInNewTab: Boolean): AbstractFindUsagesDialog {
            return super.getFindUsagesDialog(isSingleFile, toShowInNewTab, mustOpenInNewTab)
        }

        override fun processUsagesInText(element: PsiElement, processor: Processor<UsageInfo>, searchScope: GlobalSearchScope): Boolean {
            return super.processUsagesInText(element, processor, searchScope)
        }
    }

}