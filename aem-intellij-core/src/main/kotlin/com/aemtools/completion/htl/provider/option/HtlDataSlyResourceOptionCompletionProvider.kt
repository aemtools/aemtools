package com.aemtools.completion.htl.provider.option

import com.aemtools.common.constant.const
import com.aemtools.common.util.findParentByType
import com.aemtools.completion.htl.CompletionPriority.RESOURCE_TYPE
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.util.getHtlVersion
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtlDataSlyResourceOptionCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    val currentPosition = parameters.position
    val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
        ?: return

    val names = hel.getOptions().map { it.name() }
        .filterNot { it == "" }

    val htlVersion = currentPosition.project.getHtlVersion()
    val dataSlyResourceOptions = HtlAttributesRepository.getAttributesData(htlVersion)
        .filter { it.name == const.htl.DATA_SLY_RESOURCE }
        .flatMap { it.options ?: listOf() }
    val options = dataSlyResourceOptions + HtlAttributesRepository.getHtlOptions(htlVersion)

    val completionVariants = options
        .filterNot { names.contains(it.name) }
        .map(HtlOption::toLookupElement)
        .map {
          if (it.lookupString in dataSlyResourceOptions.optionNames()) {
            PrioritizedLookupElement.withPriority(it, RESOURCE_TYPE)
          } else {
            it
          }
        }

    result.addAllElements(completionVariants)

    result.stopHere()
  }

  private fun List<HtlOption>.optionNames() = this.map { it.name }

}
