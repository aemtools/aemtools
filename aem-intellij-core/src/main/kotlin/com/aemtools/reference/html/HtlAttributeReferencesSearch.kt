package com.aemtools.reference.html

import com.aemtools.common.constant.const.htl.DATA_SLY_LIST
import com.aemtools.common.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.common.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.lang.htl.file.HtlFileType
import com.aemtools.lang.util.extractItemAndItemListNames
import com.aemtools.lang.util.htlAttributeName
import com.aemtools.lang.util.htlVariableName
import com.aemtools.lang.util.isHtlAttribute
import com.aemtools.lang.util.isHtlDeclarationAttribute
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.Processor

/**
 * @author Dmytro Primshyts
 */
class HtlAttributeReferencesSearch : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>() {

  override fun processQuery(queryParameters: ReferencesSearch.SearchParameters, consumer: Processor<in PsiReference>) {
    val attribute = queryParameters.elementToSearch as? XmlAttribute
        ?: return

    if (!attribute.isHtlAttribute() || !attribute.isHtlDeclarationAttribute()) {
      return
    }

    val htlAttributeName = attribute.htlAttributeName()

    when (htlAttributeName) {
      in listOf(DATA_SLY_LIST, DATA_SLY_REPEAT) -> {
        val (item, itemList) = extractItemAndItemListNames(attribute.name)
        val scope = queryParameters.effectiveSearchScope
        val optimizer = queryParameters.optimizer
        optimizer.searchWord(item, scope, true, attribute)
        optimizer.searchWord(itemList, scope, true, attribute)
      }
      DATA_SLY_TEMPLATE -> {
        val name = attribute.htlVariableName()
            ?: return

        val scope = GlobalSearchScope.getScopeRestrictedByFileTypes(
            GlobalSearchScope.projectScope(attribute.project),
            HtlFileType)
        val optimizer = queryParameters.optimizer
        optimizer.searchWord(name, scope, true, attribute)
      }
      else -> {
        val name = attribute.htlVariableName()
            ?: return
        val scope = queryParameters.effectiveSearchScope
        val optimizer = queryParameters.optimizer
        optimizer.searchWord(name, scope, true, attribute)
      }
    }
  }
}
