package com.aemtools.analysis.htl.callchain.typedescriptor.template

import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.index.model.TemplateDefinition
import com.intellij.codeInsight.lookup.LookupElement

/**
 * Descriptor of Htl template.
 *
 * @author Dmytro Troynikov
 */
class TemplateTypeDescriptor(val template: TemplateDefinition) : TypeDescriptor {
    fun parameters() = template.parameters

    override fun myVariants(): List<LookupElement> = emptyList()
    override fun subtype(identifier: String): TypeDescriptor = TypeDescriptor.empty()

    override fun name(): String = template.name

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false
    override fun isMap(): Boolean = false
}