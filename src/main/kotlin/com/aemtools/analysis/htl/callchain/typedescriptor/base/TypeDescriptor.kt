package com.aemtools.analysis.htl.callchain.typedescriptor.base

import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiUnresolvedTypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember
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
     * Check if current type is not empty.
     */
    fun isNotEmpty() = !isEmpty()

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
     * Get declaration element.
     */
    fun referencedElement(): PsiElement? = null

    companion object {
        private val EMPTY_DESCRIPTOR = EmptyTypeDescriptor()

        fun empty(): TypeDescriptor = EMPTY_DESCRIPTOR

        fun named(name: String, psiMember: PsiMember, psiType: PsiType) = JavaPsiUnresolvedTypeDescriptor(name, psiMember, psiType)

        fun named(name: String) = NamedTypeDescriptor(name)
    }

    fun asResolutionResult(): ResolutionResult = ResolutionResult(predefined = myVariants())

}
