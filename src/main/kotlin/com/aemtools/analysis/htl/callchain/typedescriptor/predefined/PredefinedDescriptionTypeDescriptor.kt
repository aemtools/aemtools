package com.aemtools.analysis.htl.callchain.typedescriptor.predefined

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.predefined.PredefinedCompletion
import com.intellij.codeInsight.lookup.LookupElement

/**
 * @author Dmytro Troynikov
 */
class PredefinedDescriptionTypeDescriptor(val predefined: PredefinedCompletion) : BaseTypeDescriptor() {

  override fun myVariants(): List<LookupElement> = emptyList()

  override fun subtype(identifier: String): TypeDescriptor = TypeDescriptor.empty()

  override fun documentation(): String? = predefined.documentation

}
