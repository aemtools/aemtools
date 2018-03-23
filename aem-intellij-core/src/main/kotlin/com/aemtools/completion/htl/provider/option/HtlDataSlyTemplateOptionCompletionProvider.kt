package com.aemtools.completion.htl.provider.option

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * Ends completion for options inside of data-sly-template.
 *
 * @author Dmytro Troynikov
 */
object HtlDataSlyTemplateOptionCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext?,
                              result: CompletionResultSet) {
    result.stopHere()
  }
}
