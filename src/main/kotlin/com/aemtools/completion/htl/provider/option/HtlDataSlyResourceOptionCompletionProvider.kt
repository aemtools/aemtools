package com.aemtools.completion.htl.provider.option

import com.aemtools.completion.htl.CompletionPriority.RESOURCE_TYPE
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlDataSlyResourceOptionCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    val currentPosition = parameters.position
    val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
        ?: return

    val names = hel.getOptions().map { it.name() }
        .filterNot { it == "" }

    // todo temporary solution
    val resourceType = HtlOption("resourceType", "string", "", emptyList(), "")
    val options = listOf(resourceType) + HtlAttributesRepository.getHtlOptions()

    val completionVariants = options
        .filterNot { names.contains(it.name) }
        .map(HtlOption::toLookupElement)
        .map {
          if (it.lookupString == "resourceType") {
            PrioritizedLookupElement.withPriority(it, RESOURCE_TYPE)
          } else {
            it
          }
        }

    result.addAllElements(completionVariants)

    result.stopHere()
  }

}
