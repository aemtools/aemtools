package com.aemtools.completion.htl.provider.option

import com.aemtools.completion.htl.predefined.HtlELPredefined
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlContextOptionAssignmentCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext?,
                              result: CompletionResultSet) {
    val variants = HtlELPredefined.CONTEXT_VALUES.map {
      LookupElementBuilder.create(it.completionText)
          .withTypeText("HTL Context Value")
    }

    result.addAllElements(variants)
  }
}
