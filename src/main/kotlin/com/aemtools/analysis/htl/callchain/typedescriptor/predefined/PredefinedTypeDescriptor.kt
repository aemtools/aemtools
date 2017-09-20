package com.aemtools.analysis.htl.callchain.typedescriptor.predefined

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.CompletionPriority
import com.aemtools.completion.htl.predefined.PredefinedCompletion
import com.aemtools.util.withPriority
import com.intellij.codeInsight.lookup.LookupElement

/**
 * Type Descriptor backed by list of [PredefinedCompletion] objects.
 *
 * @author Dmytro Troynikov
 */
class PredefinedTypeDescriptor(val predefined: List<PredefinedCompletion>) : BaseTypeDescriptor() {

  override fun myVariants(): List<LookupElement> {
    return predefined.map(PredefinedCompletion::toLookupElement)
        .map { it.withPriority(CompletionPriority.PREDEFINED_PARAMETER) }
  }

  override fun subtype(identifier: String): TypeDescriptor =
      predefined.find { it.completionText == identifier }?.asTypeDescriptor()
          ?: TypeDescriptor.empty()

}
