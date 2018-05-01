package com.aemtools.completion.small.clientlibraryfolder.provider

import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.hasAttribute
import com.aemtools.completion.small.inserthandler.JcrArrayInsertHandler
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object ClientLibraryFolderCompletionProvider : CompletionProvider<CompletionParameters>() {

  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val tag = parameters.position.findParentByType(XmlTag::class.java) ?: return

    attributes.filterNot { attributeName ->
      tag.hasAttribute { it.name == attributeName }
    }.map {
      LookupElementBuilder.create(it)
          .withInsertHandler(JcrArrayInsertHandler())
    }.apply {
      if (this.isNotEmpty()) {
        result.addAllElements(this)
        result.stopHere()
      }
    }
  }

  val attributes: List<String> = listOf(
      "channels",
      "categories",
      "dependencies",
      "embed"
  )

}
