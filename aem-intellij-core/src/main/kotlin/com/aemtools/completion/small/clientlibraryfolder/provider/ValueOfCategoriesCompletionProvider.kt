package com.aemtools.completion.small.clientlibraryfolder.provider

import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.index.ClientLibraryIndexFacade.getAllClientLibraryModels
import com.aemtools.lang.jcrproperty.psi.JpArray
import com.aemtools.lang.jcrproperty.psi.JpArrayValue
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * Provides completion for `categories` attribute inside
 * of client library folder.
 *
 * @author Dmytro Primshyts
 */
object ValueOfCategoriesCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }
    val position = parameters.position
    val siblings = position.findParentByType(JpArray::class.java)
        ?.findChildrenByType(JpArrayValue::class.java)
        ?.map { it.text.trim() }
        ?: emptyList()

    val models = getAllClientLibraryModels(position.project)

    result.addAllElements(models.flatMap {
      it.categories
    }
        .filterNot { it in siblings }
        .map { lookupElement(it) })
    result.stopHere()
  }

}
