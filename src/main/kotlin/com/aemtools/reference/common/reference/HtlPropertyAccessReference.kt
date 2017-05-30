package com.aemtools.reference.common.reference

import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

/**
 * @author Dmytro Troynikov
 */
class HtlPropertyAccessReference(
        val propertyAccess: PropertyAccessMixin,
        val actualElement: PsiElement,
        textRange: TextRange,
        val referencedElement: PsiElement,
        soft: Boolean = true

) : PsiReferenceBase<PropertyAccessMixin>(propertyAccess, textRange, soft) {

    override fun resolve(): PsiElement? = referencedElement

    override fun getVariants(): Array<Any> = emptyArray()

}