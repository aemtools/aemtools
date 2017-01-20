package com.aemtools.analysis.htl.callchain

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.rawchainprocessor.JavaRawCallChainProcessor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.completion.htl.completionprovider.PredefinedVariables
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.java.JavaSearch
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
object HtlCallChainResolver {

    fun resolveCallChain(propertyAccessMixin: PropertyAccessMixin): ResolutionResult? {
        val callChain = callChain(propertyAccessMixin)
        return null
    }

    private fun callChain(propertyAccessMixin: PropertyAccessMixin): CallChain? {
        val rawCallChain: LinkedList<RawChainUnit> = propertyAccessMixin.callChain()

        val inputType = resolveInputType(rawCallChain)

        val result = JavaRawCallChainProcessor.processChain(rawCallChain)

        return null

//        return rawChainProcessor?.processChain(rawCallChain)
    }

    private fun resolveInputType(rawCallChain: LinkedList<RawChainUnit>): TypeDescriptor {
        if (rawCallChain.isEmpty()) {
            return TypeDescriptor.empty()
        }

        val firstChainUnit = rawCallChain.first()

        val firstUnitDeclaration = firstChainUnit.myDeclaration
        if (firstUnitDeclaration != null) {
            val attribute = firstUnitDeclaration.xmlAttribute
            val useClass = attribute.resolveUseClass()
            if (useClass != null) {
                val psiClass = JavaSearch.findClass(useClass, attribute.project)
                if (psiClass != null) {

                    return JavaPsiClassTypeDescriptor(psiClass)
                }
            }
        }

        val firstRawElement = firstChainUnit.myCallChain.firstOrNull()
        if (firstRawElement != null) {
            val varName = (firstRawElement as VariableNameMixin).variableName()
            if (varName != null) {
                val predefinedVariableClass = PredefinedVariables
                        .resolveByIdentifier(varName, firstRawElement.project)
                if (predefinedVariableClass != null) {
                    return JavaPsiClassTypeDescriptor(predefinedVariableClass)
                }
            }
        }

        return TypeDescriptor.empty()
    }

}