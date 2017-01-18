package com.aemtools.completion.htl.callchain.typedescriptor

import com.intellij.codeInsight.lookup.LookupElement

/**
 * Type Descriptor with predefined set of variants.
 * No subtype available.
 * @author Dmytro_Troynikov
 */
class PredefinedVariantsTypeDescriptor(val variants: List<LookupElement>) : TypeDescriptor(){
    override fun myVariants(): List<LookupElement> = variants

    override fun subtype(identifier: String): TypeDescriptor = empty()
}