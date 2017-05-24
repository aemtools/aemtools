package com.aemtools.lang.htl.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

/**
 * @author Dmytro_Troynikov
 */
abstract class VariableNameMixin(node: ASTNode)
    : HtlELNavigableMixin(node),
        PsiNamedElement {

    override fun getName(): String? {
        return super.getName()
    }

    override fun setName(name: String): PsiElement {
        return this
    }

    open fun variableName(): String = text

    override fun isEquivalentTo(another: PsiElement?): Boolean {
        val other = another as? VariableNameMixin
                ?: return false

        return variableName() == other.variableName()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? VariableNameMixin)?.variableName() == variableName()
    }

    override fun hashCode(): Int {
        return variableName().hashCode()
    }
}