package com.aemtools.reference.html

import com.aemtools.completion.util.*
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiReference
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.Processor

/**
 * @author Dmytro Troynikov
 */
class HtlAttributeReferencesSearch : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>() {
    override fun processQuery(queryParameters: ReferencesSearch.SearchParameters, consumer: Processor<PsiReference>) {
        val attribute = queryParameters.elementToSearch as? XmlAttribute
                ?: return

        if (!attribute.isHtlAttribute() || !attribute.isHtlDeclarationAttribute()) {
            return
        }

        val htlAttributeName = attribute.htlAttributeName()

        if (htlAttributeName in listOf(DATA_SLY_LIST, DATA_SLY_REPEAT)) {
            val (item, itemList) = extractItemAndItemListNames(attribute.name)
            val scope = queryParameters.effectiveSearchScope
            val optimizer = queryParameters.optimizer
            optimizer.searchWord(item, scope, false, attribute)
            optimizer.searchWord(itemList, scope, false, attribute)
        } else {
            val name = attribute.htlVariableName()
                    ?: return
            val scope = queryParameters.effectiveSearchScope
            val optimizer = queryParameters.optimizer
            optimizer.searchWord(name, scope, false, attribute)
        }
    }
}