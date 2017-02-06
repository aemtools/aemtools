package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiType

/**
 * Base Type Descriptor.
 */
interface TypeDescriptor {

    fun myVariants(): List<LookupElement>

    fun subtype(identifier: String): TypeDescriptor

    fun name(): String

    fun isEmpty() = myVariants().isEmpty()

    fun isArray(): Boolean

    fun isIterable(): Boolean

    fun isMap(): Boolean

    companion object {
        private val EMPTY_DESCRIPTOR = EmptyTypeDescriptor()

        fun empty(): TypeDescriptor = EMPTY_DESCRIPTOR

        fun named(name: String, psiMember: PsiMember, psiType: PsiType) = JavaPsiUnresolvedTypeDescriptor(name, psiMember, psiType)

        fun named(name: String) = NamedTypeDescriptor(name)
    }

    fun asResolutionResult(): ResolutionResult = ResolutionResult()

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