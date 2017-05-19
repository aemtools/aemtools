package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiType

/**
 * Base Type Descriptor.
 */
interface TypeDescriptor {

    /**
     * Return list of available [LookupElement] objects.
     */
    fun myVariants(): List<LookupElement>

    /**
     * Resolve subtype by identifier.
     */
    fun subtype(identifier: String): TypeDescriptor

    /**
     * The name of current type.
     */
    fun name(): String

    /**
     * Check if current type doesn't have variants.
     */
    fun isEmpty() = myVariants().isEmpty()

    /**
     * Check if the type is array type.
     */
    fun isArray(): Boolean

    /**
     * Check if the type is iterable (i.e. may be used within *data-sly-list* or *data-sly-repeat*).
     */
    fun isIterable(): Boolean

    /**
     * Check if the type is a map.
     */
    fun isMap(): Boolean

    /**
     * Get description of the type.
     */
    fun documentation(): String? = null

    /**
     * Get reference to declaration of current type descriptor.
     */
    fun reference(): PsiReference? = null

    companion object {
        private val EMPTY_DESCRIPTOR = EmptyTypeDescriptor()

        fun empty(): TypeDescriptor = EMPTY_DESCRIPTOR

        fun named(name: String, psiMember: PsiMember, psiType: PsiType) = JavaPsiUnresolvedTypeDescriptor(name, psiMember, psiType)

        fun named(name: String) = NamedTypeDescriptor(name)
    }

    fun asResolutionResult(): ResolutionResult = ResolutionResult(predefined = myVariants())

}

open class NamedTypeDescriptor(private val myName: String)
    : EmptyTypeDescriptor() {
    override fun name(): String = myName
}

open class JavaPsiUnresolvedTypeDescriptor(private val myName: String,
                                           val psiMember: PsiMember,
                                           val psiType: PsiType) : EmptyTypeDescriptor() {
    override fun name(): String = myName
}

open class EmptyTypeDescriptor : TypeDescriptor {
    override fun isArray(): Boolean = false
    override fun isIterable(): Boolean = false
    override fun isMap(): Boolean = false

    override fun myVariants(): List<LookupElement> = listOf()

    override fun subtype(identifier: String): TypeDescriptor = TypeDescriptor.empty()

    override fun name(): String = ""
}

interface ArrayTypeDescriptor : TypeDescriptor {
    fun arrayType(): TypeDescriptor
}

interface IterableTypeDescriptor : TypeDescriptor {
    fun iterableType(): TypeDescriptor
}

interface MapTypeDescriptor : TypeDescriptor {
    fun keyType(): TypeDescriptor
    fun valueType(): TypeDescriptor
}