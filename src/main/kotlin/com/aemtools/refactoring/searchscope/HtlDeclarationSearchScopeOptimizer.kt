package com.aemtools.refactoring.searchscope

import com.aemtools.completion.util.getHtlFile
import com.aemtools.completion.util.htlAttributeName
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ScopeOptimizer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationSearchScopeOptimizer : ScopeOptimizer {

  companion object {
    val FILE_SCOPE_DECLARATION: List<String> = listOf(
        DATA_SLY_USE,
        DATA_SLY_TEST,
        DATA_SLY_LIST,
        DATA_SLY_REPEAT
    )
  }

  override fun getScopeToExclude(element: PsiElement): GlobalSearchScope? {
    if (element is XmlAttribute
        && element.htlAttributeName() in FILE_SCOPE_DECLARATION) {
      val originalFile = element.containingFile
      val htlFile = originalFile.getHtlFile()
          ?: return null

      return GlobalSearchScope.notScope(GlobalSearchScope.filesScope(
          element.project,
          mutableListOf(originalFile.virtualFile, htlFile.virtualFile)))
    }
    return null
  }
}
