package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.codeInsight.lookup.LookupElement
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * @author Dmytro Troynikov
 */
class MergedTypeDescriptor(val type1: TypeDescriptor,
                           val type2: TypeDescriptor) : TypeDescriptor {
    override fun myVariants(): List<LookupElement> =
            type1.myVariants() + type2.myVariants()

    override fun subtype(identifier: String): TypeDescriptor {
        val subtype1 = type1.subtype(identifier)
        return if (subtype1 !is EmptyTypeDescriptor) {
            subtype1
        } else {
            type2.subtype(identifier)
        }
    }

    override fun name(): String {
        val name1 = type1.name()
        return if (name1.isNotBlank()) {
            name1
        } else {
            type2.name()
        }
    }

    override fun isArray(): Boolean =
            type1.isArray() or type2.isArray()

    override fun isIterable(): Boolean =
            type1.isIterable() or type2.isIterable()

    override fun isMap(): Boolean =
            type1.isMap() or type2.isMap()

    override fun asResolutionResult(): ResolutionResult =
            type1.asResolutionResult() + type2.asResolutionResult()
}