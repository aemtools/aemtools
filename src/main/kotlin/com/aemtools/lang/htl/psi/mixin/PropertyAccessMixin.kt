package com.aemtools.lang.htl.psi.mixin

import com.aemtools.analysis.htl.callchain.HtlCallChainResolver
import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.model.declaration.DeclarationAttributeType
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.extractPropertyAccess
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.lang.htl.psi.chain.RawChainUnit
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

    /**
     * Returns call chain of current element.
     */
    fun callChain(): LinkedList<RawChainUnit> {
        var result = LinkedList<RawChainUnit>()
        val myChain = LinkedList(listOf(*this.children))

        val firstElement = myChain.first() as VariableNameMixin
        val firstName = firstElement.variableName()

        val declaration = FileVariablesResolver.findDeclaration(firstName, firstElement, this.containingFile)

        if (declaration != null
                && declaration.attributeType !in listOf(DeclarationAttributeType.LIST_HELPER, DeclarationAttributeType.REPEAT_HELPER)) {
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

        result.add(RawChainUnit(LinkedList(), declaration))
        return result
    }

}
