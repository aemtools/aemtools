package com.aemtools.completion.htl.callchain.typedescriptor

import com.intellij.codeInsight.lookup.LookupElement

/**
 * Base Type Descriptor.
 */
interface TypeDescriptor {

    fun myVariants(): List<LookupElement>

    fun subtype(identifier: String): TypeDescriptor

    fun name(): String

    fun isEmpty() = myVariants().isEmpty()

    fun isArray(): Boolean

    fun isList(): Boolean

    fun isMap(): Boolean

    companion object {
        private val EMPTY_DESCRIPTOR = EmptyTypeDescriptor()

        fun empty(): TypeDescriptor = EMPTY_DESCRIPTOR

        fun named(name: String) = NamedTypeDescriptor(name)
    }

}

open class NamedTypeDescriptor(private val myName: String) : EmptyTypeDescriptor() {
    override fun name(): String = myName
}

open class EmptyTypeDescriptor : TypeDescriptor {
    override fun isArray(): Boolean = false
    override fun isList(): Boolean = false
    override fun isMap(): Boolean = false

    override fun myVariants(): List<LookupElement> = listOf()

    override fun subtype(identifier: String): TypeDescriptor = TypeDescriptor.empty()

    override fun name(): String = ""
}

interface ArrayTypeDescriptor : TypeDescriptor {
    fun arrayType(): TypeDescriptor
}

interface ListTypeDescriptor : TypeDescriptor {
    fun listType(): TypeDescriptor
}

interface MapTypeDescriptor : TypeDescriptor {
    fun keyType(): TypeDescriptor
    fun valueType(): TypeDescriptor
}