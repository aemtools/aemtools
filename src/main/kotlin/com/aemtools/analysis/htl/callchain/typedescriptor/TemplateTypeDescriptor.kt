package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * @author Dmytro Troynikov
 */
class TemplateTypeDescriptor(val template: List<TemplateDefinition>)
    : TypeDescriptor {
    override fun myVariants(): List<LookupElement> {
        return template.map {
            LookupElementBuilder.create(it.name)
                    .withTypeText("HTL Template")
                    .withIcon(HtlIcons.HTL_FILE)
        }
    }

    override fun subtype(identifier: String): TypeDescriptor {
        return TypeDescriptor.empty()
    }

    override fun name(): String = "name"

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = false

    override fun asResolutionResult(): ResolutionResult =
            ResolutionResult(null, myVariants())

}