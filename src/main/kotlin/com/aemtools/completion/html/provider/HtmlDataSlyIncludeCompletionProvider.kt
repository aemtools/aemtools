package com.aemtools.completion.html.provider

import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.completion.util.relativeTo
import com.aemtools.index.HtlIndexFacade
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtmlDataSlyIncludeCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val files = HtlIndexFacade.includableFiles(parameters.originalFile)

    val dirName = parameters.originalFile.containingDirectory.virtualFile.path
        .normalizeToJcrRoot()

    val variants = files.map {
      LookupElementBuilder.create(
          it.virtualFile.path.normalizeToJcrRoot()
              .relativeTo(dirName))
          .withTypeText(it.fileType.name)
          .withTailText("(${it.virtualFile.path.normalizeToJcrRoot()})", true)
          .withIcon(it.getIcon(0))
    }
    result.addAllElements(variants)
  }
}
