package com.aemtools.completion.htl.provider

import com.aemtools.analysis.htl.callchain.elements.segment.resolveSelectedItem
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object HtlElMemberAccessCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    val currentPosition = parameters.position
    val resolutionResult = resolve(currentPosition)

    resolutionResult.predefined?.let {
      result.addAllElements(it)
    }

    result.stopHere()
  }

  /**
   * Resolve given psi element.
   *
   * @param element the element
   * @return resolution result object
   */
  fun resolve(element: PsiElement): ResolutionResult {
    val propertyAccessElement = element.findParentByType(PropertyAccessMixin::class.java)
        ?: return ResolutionResult()

    val chain = propertyAccessElement.callChain()
        ?: return ResolutionResult()
    val lastSegment = chain.callChainSegments.lastOrNull()
        ?: return ResolutionResult()

    return lastSegment.resolveSelectedItem()
  }

}
