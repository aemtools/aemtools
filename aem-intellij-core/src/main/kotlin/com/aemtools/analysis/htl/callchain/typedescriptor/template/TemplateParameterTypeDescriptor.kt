package com.aemtools.analysis.htl.callchain.typedescriptor.template

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.codeinsight.htl.model.HtlTemplateParameterDeclaration
import com.intellij.codeInsight.lookup.LookupElement

/**
 * Descriptor of template parameter.
 *
 * @author Dmytro Primshyts
 */
class TemplateParameterTypeDescriptor(
    val declaration: HtlTemplateParameterDeclaration) : BaseTypeDescriptor() {

  override fun myVariants(): List<LookupElement> = emptyList()

  override fun subtype(identifier: String): TypeDescriptor =
      TypeDescriptor.empty()

}
