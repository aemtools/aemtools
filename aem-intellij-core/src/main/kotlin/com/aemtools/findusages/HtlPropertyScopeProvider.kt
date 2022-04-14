package com.aemtools.findusages

import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.impl.search.CustomPropertyScopeProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope

/**
 * Enables *Find Usages* action to search in HTL files.
 * @author Dmytro Primshyts
 */
class HtlPropertyScopeProvider : CustomPropertyScopeProvider {
  override fun getScope(project: Project): SearchScope =
      GlobalSearchScope.getScopeRestrictedByFileTypes(
          GlobalSearchScope.projectScope(project),
          HtmlFileType.INSTANCE, HtlFileType
      )
}
