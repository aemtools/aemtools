package com.aemtools.lang.htl.psi.mixin

import com.intellij.lang.ASTNode

/**
* @author Dmytro_Troynikov
*/
abstract class AccessIdentifierMixin(node: ASTNode) : VariableNameMixin(node) {

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
    override fun variableName() : String {
        return when {
            text.startsWith(".") ->
                text.substring(1)
            text.startsWith("['") || text.startsWith("[\"") ->
                 text.substring(2, text.length - 2)
            else -> ""
        }
    }

}