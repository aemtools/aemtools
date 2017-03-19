package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor.Companion.empty
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.htl.predefined.PredefinedCompletion
import com.intellij.codeInsight.lookup.LookupElement
import org.apache.commons.lang.StringUtils

/**
 * Type Descriptor with predefined set of variants.
 * No subtype available.
 * @author Dmytro_Troynikov
 */
class PredefinedVariantsTypeDescriptor(val variants: List<LookupElement>) : TypeDescriptor {
    override fun name(): String = StringUtils.EMPTY

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = false

    override fun myVariants(): List<LookupElement> = variants

    override fun subtype(identifier: String): TypeDescriptor = empty()

    override fun asResolutionResult(): ResolutionResult =
            ResolutionResult(null, variants)

}

/**
 * Type Descriptor backed by list of [PredefinedCompletion] objects.
 */
class PredefinedTypeDescriptor(val predefined: List<PredefinedCompletion>) : TypeDescriptor {
    override fun myVariants(): List<LookupElement> {
        return predefined.map(PredefinedCompletion::toLookupElement)
    }

    override fun subtype(identifier: String): TypeDescriptor =
        predefined.find { it.completionText == identifier }?.asTypeDescriptor()
        ?: empty()

    override fun name(): String = ""

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = false
}

class PredefinedDescriptionTypeDescriptor(val predefined: PredefinedCompletion) : TypeDescriptor {
    override fun myVariants(): List<LookupElement> = emptyList()

    override fun subtype(identifier: String): TypeDescriptor = empty()

    override fun name(): String = predefined.type ?: ""

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = false

    override fun documentation(): String? = predefined.documentation

}