package com.aemtools.completion.htl.provider.option

import com.aemtools.common.util.findParentByType
import com.aemtools.completion.htl.CompletionPriority
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.getHtlVersion
import com.aemtools.lang.util.notSupportsHtlVersion
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.util.ProcessingContext

/**
 * @author Kostiantyn Diachenko
 */
class HtlDataSlyIterableOptionCompletionProvider(
    private val iterableAttributeName: String
) : CompletionProvider<CompletionParameters>() {

  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    val currentPosition = parameters.position
    val project = currentPosition.project
    if (project.notSupportsHtlVersion(HtlVersion.V_1_4)) {
      return
    }
    val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
        ?: return

    val names = hel.getOptions().map { it.name() }
        .filterNot { it == "" }

    val dataSlyIterableOptions = HtlAttributesRepository.getAttributesData(project.getHtlVersion())
        .filter { it.name == iterableAttributeName }
        .flatMap { it.options ?: listOf() }

    val completionVariants = dataSlyIterableOptions
        .filterNot { names.contains(it.name) }
        .map(HtlOption::toLookupElement)
        .map {
          if (it.lookupString in dataSlyIterableOptions.optionNames()) {
            PrioritizedLookupElement.withPriority(it, CompletionPriority.ITERABLE_OPTION)
          } else {
            it
          }
        }

    result.addAllElements(completionVariants)

    result.stopHere()
  }

  private fun List<HtlOption>.optionNames() = this.map { it.name }
}
