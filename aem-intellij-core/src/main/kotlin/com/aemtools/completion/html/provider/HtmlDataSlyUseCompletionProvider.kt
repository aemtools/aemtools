package com.aemtools.completion.html.provider

import com.aemtools.completion.htl.provider.SlyUseCompletionProvider
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * Provides available "data-sly-use" variants.
 * @author Dmytro_Troynikov
 */
object HtmlDataSlyUseCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?,
                              result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    SlyUseCompletionProvider.addCompletionVariants(parameters, context, result)
  }

}
