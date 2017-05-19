package com.aemtools.analysis.htl.callchain.typedescriptor.template

import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.completion.htl.model.declaration.HtlTemplateParameterDeclaration
import com.intellij.codeInsight.lookup.LookupElement

/**
 * Descriptor of template parameter.
 *
 * @author Dmytro Troynikov
 */
class TemplateParameterTypeDescriptor(
        val declaration: HtlTemplateParameterDeclaration) : TypeDescriptor {

    override fun myVariants(): List<LookupElement> = emptyList()

    override fun subtype(identifier: String): TypeDescriptor =
            TypeDescriptor.empty()

    override fun name(): String = declaration.variableName
    override fun isArray(): Boolean = false
    override fun isIterable(): Boolean = false
    override fun isMap(): Boolean = false

}