package com.aemtools.completion.small.clientlibraryfolder.provider

import com.aemtools.index.HtlIndexFacade.getAllClientLibraryModels
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object ValueOfCategoriesCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }
    val position = parameters.position
    val models = getAllClientLibraryModels(position.project)

    result.addAllElements(models.flatMap {
      it.categories
    }.map { LookupElementBuilder.create(it) })
    result.stopHere()
  }

}
