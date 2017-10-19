package com.aemtools.refactoring.searchscope

import com.aemtools.completion.util.getHtlFile
import com.aemtools.completion.util.htlAttributeName
import com.intellij.psi.PsiElement
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.UseScopeEnlarger
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationSearchScopeEnlarger : UseScopeEnlarger() {
  override fun getAdditionalUseScope(element: PsiElement): SearchScope? {
    if (element is XmlAttribute
        && element.htlAttributeName() in HtlDeclarationSearchScopeOptimizer.FILE_SCOPE_DECLARATION) {
      val originalFile = element.containingFile
      val htlFile = originalFile.getHtlFile()
          ?: return null

      return LocalSearchScope(arrayOf(htlFile,
          originalFile,
          *htlFile.children, *originalFile.children))
    }
    return null
  }

}
