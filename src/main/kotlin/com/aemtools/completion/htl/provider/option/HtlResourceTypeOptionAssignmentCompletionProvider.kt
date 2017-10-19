package com.aemtools.completion.htl.provider.option

import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.index.model.AemComponentDefinition.Companion.toLookupElement
import com.aemtools.index.search.AemComponentSearch
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.util.ProcessingContext
import org.apache.commons.lang.StringUtils

/**
 * @author Dmytro Troynikov
 */
object HtlResourceTypeOptionAssignmentCompletionProvider
  : CompletionProvider<CompletionParameters>() {

  private const val BASE_LINE: Double = 1.0
  private const val ONE_HUNDRED: Double = 100.0

  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    val myDirectory = parameters.position.containingFile.originalFile.containingDirectory.virtualFile
        .path

    val myNormalizedDirectory = myDirectory.normalizeToJcrRoot()

    val declarations = AemComponentSearch
        .allComponentDeclarations(parameters.position.project)
        .filterNot {
          myNormalizedDirectory == it.resourceType()
              || myNormalizedDirectory.startsWith(it.resourceType())
        }
        .map {
          val lookupElement = it.toLookupElement()

          PrioritizedLookupElement
              .withPriority(lookupElement, calcPriority(lookupElement, myNormalizedDirectory))
        }
    result.addAllElements(declarations)
    result.stopHere()
  }

  private fun calcPriority(lookupElement: LookupElement, myDirectory: String): Double {
    return BASE_LINE - StringUtils.getLevenshteinDistance(lookupElement.lookupString, myDirectory)
        .toDouble() / ONE_HUNDRED
  }

}
