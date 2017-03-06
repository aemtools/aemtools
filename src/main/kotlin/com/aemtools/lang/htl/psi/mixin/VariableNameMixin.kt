package com.aemtools.lang.htl.psi.mixin

import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

/**
 * @author Dmytro_Troynikov
 */`
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

    /**
     * Resolve current element's PsiClass
     * File context is preferred resolution source as custom variables may override
     * standard context objects.
     * @return the [PsiClass] of current variable __null__ if resolution failed
     */
    fun resolve(): ResolutionResult {
        var result = FileVariablesResolver.resolveVariable(this)

        if (result.isEmpty()) {
            result = ResolutionResult(
                    PredefinedVariables
                            .resolveByIdentifier(this.variableName(),
                                    project))
        }

        return result
    }

}