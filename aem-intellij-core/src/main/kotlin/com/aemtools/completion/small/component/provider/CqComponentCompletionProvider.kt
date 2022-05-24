package com.aemtools.completion.small.component.provider

import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.hasAttribute
import com.aemtools.service.repository.inmemory.CqComponentRepository
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext

/**
 * Completion provider for attributes of the node with jcr:primaryType="cq:Component".
 *
 * @author Kostiantyn Diachenko
 */
object CqComponentCompletionProvider : CompletionProvider<CompletionParameters>() {

  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val tag = parameters.position.findParentByType(XmlTag::class.java) ?: return

    CqComponentRepository.getNodeProperties()
        .map { it.name }
        .filterNot { attributeName ->
          tag.hasAttribute { it.name == attributeName }
        }.map {
          lookupElement(it).withInsertHandler(XmlAttributeInsertHandler())
        }.apply {
          if (this.isNotEmpty()) {
            result.addAllElements(this)
            result.stopHere()
          }
        }
  }

}
