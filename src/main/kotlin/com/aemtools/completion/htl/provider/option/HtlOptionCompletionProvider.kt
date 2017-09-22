package com.aemtools.completion.htl.provider.option

import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlOptionCompletionProvider : CompletionProvider<CompletionParameters>() {

  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext?,
                              result: CompletionResultSet) {
    val currentPosition = parameters.position
    val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
        ?: return

    val names = hel.getOptions().map { it.name() }
        .filterNot { it == "" }

    val completionVariants = HtlAttributesRepository.getHtlOptions()
        .filterNot { names.contains(it.name) }
        .map(HtlOption::toLookupElement)

    result.addAllElements(completionVariants)
    result.stopHere()
  }

}
