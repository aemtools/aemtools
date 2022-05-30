package com.aemtools.common.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.util.ProcessingContext

/**
 * Functional completion provider is indented to be used as
 * a replacement of full blown [CompletionProvider] for cases
 * when completion logic is simple (e.g. static list of completion variants).
 *
 * @author Dmytro Primshyts
 */
open class BaseCompletionProvider(
    private val completionProvider: (
        CompletionParameters,
        ProcessingContext,
        CompletionResultSet
    ) -> List<LookupElement>,
    private val shouldStop: Boolean = false
) : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    completionProvider.invoke(
        parameters,
        context,
        result
    ).apply {
      if (isNotEmpty()) {
        result.addAllElements(this)

        if (shouldStop) {
          result.stopHere()
        }
      }
    }

  }
}
