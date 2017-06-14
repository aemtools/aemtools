package com.aemtools.reference.htl.reference

import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateParameterTypeDescriptor
import com.aemtools.lang.htl.psi.HtlVariableName
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

/**
 * Reference to template parameter.
 *
 * @author Dmytro Troynikov
 */
class HtlTemplateParameterReference(
        val type: TemplateParameterTypeDescriptor,
        holder: PsiElement,
        range: TextRange
) : PsiReferenceBase<PsiElement>(holder, range, true) {

    override fun resolve(): PsiElement? =
            type.declaration.htlVariableNameElement

    override fun getVariants(): Array<Any> = emptyArray()

}

class HtlTemplateArgumentReference(
        val variable: HtlVariableName,
        holder: PsiElement,
        range: TextRange
) : PsiReferenceBase<PsiElement>(holder, range, true) {

    override fun resolve(): PsiElement? = variable

    override fun getVariants(): Array<Any> = emptyArray()

}
