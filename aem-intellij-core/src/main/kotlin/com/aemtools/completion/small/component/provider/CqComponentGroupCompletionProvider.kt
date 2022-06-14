package com.aemtools.completion.small.component.provider

import com.aemtools.common.completion.lookupElement
import com.aemtools.index.search.AemComponentSearch
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * Completion provider for componentGroup attribute value of jcr:primaryType="cq:Component" node.
 *
 * @author Kostiantyn Diachenko
 */
object CqComponentGroupCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val allProjectComponentGroups = AemComponentSearch.allComponentDeclarations(parameters.position.project)
        .filter { it.componentGroup != null && it.componentGroup != ".hidden" }
        .groupingBy { it.componentGroup }.eachCount()
        .map { Pair(it.key, it.value) }
        .sortedByDescending { it.second }
        .mapNotNull { it.first }

    result.addAllElements(listOf(
        ".hidden",
        *allProjectComponentGroups.toTypedArray()
    ).map {
      lookupElement(it)
    })
    result.stopHere()
  }
}
