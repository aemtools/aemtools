package com.aemtools.completion.htl.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlDataSlyUseCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext?,
                              result: CompletionResultSet) {
    val useSuggestions = SlyUseCompletionProvider.useSuggestions(parameters)
    if (useSuggestions.isNotEmpty()) {
      result.addAllElements(SlyUseCompletionProvider.useSuggestions(parameters))
      result.stopHere()
    }
  }

}
