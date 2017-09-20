package com.aemtools.findusages

import com.aemtools.completion.util.incomingReferences
import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.aemtools.reference.htl.reference.HtlDeclarationReference
import com.aemtools.reference.htl.reference.HtlListHelperReference
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchScope
import com.intellij.psi.xml.XmlAttribute

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

  /**
   * Htl attributes find usages handler.
   */
  class HtlAttributesFindUsagesHandler(val xmlAttribute: XmlAttribute) : FindUsagesHandler(xmlAttribute) {

    override fun findReferencesToHighlight(
        target: PsiElement,
        searchScope: SearchScope): MutableCollection<PsiReference> {
      return super.findReferencesToHighlight(target, searchScope)
          .filter { it is HtlDeclarationReference || it is HtlListHelperReference }
          .toMutableList()
    }

    override fun getFindUsagesOptions(dataContext: DataContext?): FindUsagesOptions {
      return super.getFindUsagesOptions(dataContext)
    }

    override fun getPrimaryElements(): Array<PsiElement> {
      return super.getPrimaryElements()
    }

    override fun getSecondaryElements(): Array<PsiElement> {
      return xmlAttribute.incomingReferences().mapNotNull {
        it.element
      }.toTypedArray()
    }

  }

}
