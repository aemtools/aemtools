package com.aemtools.lang.htl.psi.mixin

import com.aemtools.lang.htl.psi.HtlElementFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * @author Dmytro_Troynikov
 */
abstract class AccessIdentifierMixin(node: ASTNode) : VariableNameMixin(node) {

    override fun getName(): String? =
            variableName()

    override fun setName(name: String): PsiElement {
        val newText = text.replace(variableName(), name)
        val newElement = HtlElementFactory.createDotAccessIdentifier(newText, project)

        newElement?.let {
            node.replaceChild(node.firstChildNode, it)
        }

        return this
    }

    /**
     * Extracts the name of the variable
     * e.g.
     *
     * ```
     * .name -> name
     * ["name"] -> name
     * ['name'] -> name
     * ```
     * @return the variable name
     */
    override fun variableName(): String {
        return when {
            text.startsWith(".") ->
                text.substring(1)
            text.startsWith("['") || text.startsWith("[\"") ->
                text.substring(2, text.length - 2)
            else -> ""
        }
    }

}