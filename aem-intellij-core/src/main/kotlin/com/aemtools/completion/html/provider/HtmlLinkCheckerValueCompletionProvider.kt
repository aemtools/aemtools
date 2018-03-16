package com.aemtools.completion.html.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtmlLinkCheckerValueCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    result.addAllElements(listOf(
        LookupElementBuilder.create("skip"),
        LookupElementBuilder.create("valid")
    ))
    result.stopHere()
  }

}
