package com.aemtools.lang.htl.psi.mixin

import com.aemtools.analysis.htl.callchain.HtlCallChainResolver
import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.model.PropertyAccessChainUnit
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.extractPropertyAccess
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.java.JavaSearch
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
abstract class PropertyAccessMixin(node: ASTNode) : HtlELNavigableMixin(node) {

    fun accessChain(): CallChain? {
        return HtlCallChainResolver.resolveCallChain(this)
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }

    fun resolveIterable(): ResolutionResult {
        val names = callChain()
        val propertyAccessChain = LinkedList<PropertyAccessChainUnit>()

        val accessChain = accessChain()

        val firstElement = names.pop() as VariableNameMixin
        val resolutionResult = firstElement.resolve()

        if (resolutionResult.predefined != null) {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, resolutionResult, null))
        } else {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, ResolutionResult(resolutionResult.psiClass), null))
        }

        return ResolutionResult()
    }

    /**
     * Returns call chain of current element.
     */
    fun callChain(): LinkedList<RawChainUnit> {
        var result = LinkedList<RawChainUnit>()
        var myChain = LinkedList(listOf(*this.children))

        val firstElement = myChain.first() as VariableNameMixin
        val firstName = firstElement.variableName()

        val declaration = FileVariablesResolver.findDeclaration(firstName, firstElement, this.containingFile)

        if (declaration != null
                && declaration.resolutionResult.isEmpty()) {
            val propertyAccessMixin = declaration.xmlAttribute.extractHtlHel()?.extractPropertyAccess()

            // if property access mixin is available recursively obtain it's call chain
            if (propertyAccessMixin != null) {
                result = propertyAccessMixin.callChain()
            }

            if (propertyAccessMixin == null) {
                val useClass = declaration.xmlAttribute.resolveUseClass()
                if (useClass != null) {
                    result = createUseChainUnit(declaration, useClass)
                }
            }
        }

        val myChainUnit = RawChainUnit(myChain, declaration)

        return LinkedList(listOf(*result.toTypedArray(), myChainUnit))
    }

    private fun createUseChainUnit(declaration: HtlVariableDeclaration, useClass: String): LinkedList<RawChainUnit> {
        val result = LinkedList<RawChainUnit>()

        val clazz = JavaSearch.findClass(useClass, project)
        result.add(RawChainUnit(LinkedList(), HtlVariableDeclaration(
                declaration.xmlAttribute,
                declaration.variableName,
                declaration.attributeType,
                declaration.type,
                ResolutionResult(clazz)
        )))
        return result
    }

}
