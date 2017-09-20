package com.aemtools.analysis.htl.callchain.elements.segment

import com.aemtools.analysis.htl.callchain.elements.ArrayAccessIdentifierElement
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ArrayJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.IterableJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.constant.const

/**
 * Resolve selected item in current [CallChainSegment].
 *
 * @receiver [CallChainSegment]
 * @return resolution result
 */
fun CallChainSegment.resolveSelectedItem(): ResolutionResult {
  val chainElements = this.chainElements()
  if (chainElements.isEmpty()) {
    return ResolutionResult()
  }

  val selectedItem = selectedElement()
      ?: return ResolutionResult()
  val indexOfSelectedItem = chainElements.indexOf(selectedItem)

  val resolutionResult = when {
    chainElements.size > 2
        && chainElements[indexOfSelectedItem - 1] is ArrayAccessIdentifierElement ->
      with(chainElements[indexOfSelectedItem - 2].type) {
        when {
          this is ArrayJavaTypeDescriptor ->
            this.arrayType().asResolutionResult()
          this is IterableJavaTypeDescriptor ->
            this.iterableType().asResolutionResult()
          this is MapJavaTypeDescriptor ->
            this.valueType().asResolutionResult()
          else -> this.asResolutionResult()
        }
      }
    else -> chainElements[indexOfSelectedItem - 1].type.asResolutionResult()
  }

  return resolutionResult
}

/**
 * Find selected element in current [CallChainSegment].
 *
 * @receiver [CallChainSegment]
 * @return the element
 */
fun CallChainSegment.selectedElement(): CallChainElement? {
  return chainElements().find { it.name.contains(const.IDEA_STRING_CARET_PLACEHOLDER) }
}
