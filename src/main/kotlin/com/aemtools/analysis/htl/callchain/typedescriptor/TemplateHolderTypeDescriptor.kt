package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * @author Dmytro Troynikov
 */
class TemplateHolderTypeDescriptor(val templates: List<TemplateDefinition>)
    : TypeDescriptor {
    override fun myVariants(): List<LookupElement> {
        return templates.map {
            LookupElementBuilder.create(it.name)
                    .withTypeText("HTL Template")
                    .withIcon(HtlIcons.HTL_FILE)
        }
    }

    override fun subtype(identifier: String): TypeDescriptor {
        return templates.find { it.name == identifier }
                .toTypeDescriptor()
    }

    override fun name(): String = "name"

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = false

    override fun asResolutionResult(): ResolutionResult =
            ResolutionResult(null, myVariants())

    private fun TemplateDefinition?.toTypeDescriptor(): TypeDescriptor =
            if (this != null) {
                TemplateTypeDescriptor(this)
            } else {
                TypeDescriptor.empty()
            }
}

class TemplateTypeDescriptor(val template: TemplateDefinition) : TypeDescriptor {
    fun parameters() = template.parameters

    override fun myVariants(): List<LookupElement> = emptyList()
    override fun subtype(identifier: String): TypeDescriptor = TypeDescriptor.empty()

    override fun name(): String = template.name

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false
    override fun isMap(): Boolean = false
}