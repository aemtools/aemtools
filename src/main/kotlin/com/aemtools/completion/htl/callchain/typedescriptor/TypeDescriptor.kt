package com.aemtools.completion.htl.callchain.typedescriptor

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiClass

/**
 * Base Type Descriptor.
 */
abstract class TypeDescriptor() {

    abstract fun myVariants() : List<LookupElement>

    abstract fun subtype(identifier: String) : TypeDescriptor

    open fun isEmpty() = myVariants().isEmpty()

    companion object {
        private val EMPTY_DESCRIPTOR = object : TypeDescriptor() {
            override fun myVariants(): List<LookupElement> = listOf()

            override fun subtype(identifier: String): TypeDescriptor = empty()
        }

        fun empty() : TypeDescriptor = EMPTY_DESCRIPTOR
    }

}

