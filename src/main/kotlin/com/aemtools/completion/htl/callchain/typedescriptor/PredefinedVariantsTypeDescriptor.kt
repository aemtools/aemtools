package com.aemtools.completion.htl.callchain.typedescriptor

import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor.Companion.empty
import com.intellij.codeInsight.lookup.LookupElement

/**
 * Type Descriptor with predefined set of variants.
 * No subtype available.
 * @author Dmytro_Troynikov
 */
class PredefinedVariantsTypeDescriptor(val variants: List<LookupElement>) : TypeDescriptor{
    override fun name(): String {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isArray(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isList(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isMap(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun myVariants(): List<LookupElement> = variants

    override fun subtype(identifier: String): TypeDescriptor = empty()
}