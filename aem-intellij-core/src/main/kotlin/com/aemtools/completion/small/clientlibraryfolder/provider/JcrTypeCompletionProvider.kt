package com.aemtools.completion.small.clientlibraryfolder.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object JcrTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    result.addAllElements(listOf(
        "String",
        "Binary",
        "Long",
        "Double",
        "Decimal",
        "Date",
        "Boolean",
        "Name",
        "Path",
        "Reference",
        "WeakReference",
        "URI"
    ).map {
      LookupElementBuilder.create(it)
    })
  }

}
