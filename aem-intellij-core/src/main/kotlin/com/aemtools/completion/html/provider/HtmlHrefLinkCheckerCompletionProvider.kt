package com.aemtools.completion.html.provider

import com.aemtools.common.completion.lookupElement
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtmlHrefLinkCheckerCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    result.addElement(lookupElement("x-cq-linkchecker")
        .withInsertHandler(XmlAttributeInsertHandler())
    )
  }

}
