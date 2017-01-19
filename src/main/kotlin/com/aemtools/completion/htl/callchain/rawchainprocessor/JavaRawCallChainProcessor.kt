package com.aemtools.completion.htl.callchain.rawchainprocessor

import com.aemtools.completion.htl.callchain.elements.BaseChainElement
import com.aemtools.completion.htl.callchain.elements.CallChain
import com.aemtools.completion.htl.callchain.elements.CallChainElement
import com.aemtools.completion.htl.callchain.elements.CallChainSegment
import com.aemtools.completion.htl.callchain.elements.helper.chainSegment
import com.aemtools.completion.htl.callchain.typedescriptor.PredefinedVariantsTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.java.ListJavaTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.completionprovider.PredefinedVariables
import com.aemtools.lang.htl.psi.HtlArrayLikeAccess
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
object JavaRawCallChainProcessor : RawCallChainProcessor {
    override fun processChain(rawChain: LinkedList<RawChainUnit>): CallChain {
        if (rawChain.isEmpty()) {
            return CallChain.empty()
        }

        val segments = ArrayList<CallChainSegment>()

        val firstElement: RawChainUnit = rawChain.first()

        segments.add(extractFirstSegment(firstElement))

        return CallChain.empty()
    }

    private fun extractFirstSegment(rawChainUnit: RawChainUnit): CallChainSegment {
        val chainUnitDeclaration = rawChainUnit.myDeclaration
        val elements = rawChainUnit.myCallChain

        if (rawChainUnit.hasPredefinedVariants()) {
            return constructPredefinedChainSegment(rawChainUnit)
        }

        val inputType = resolveFirstType(rawChainUnit)

        // TODO: construct and return chain with empty values
        if (inputType.isEmpty()) {
        }

        if (inputType is JavaPsiClassTypeDescriptor) {
            return constructJavaChainSegment(inputType, rawChainUnit)
        } else {
            return CallChainSegment.empty()
        }
    }

    /**
     * Resolve first [PsiClass] of first call chain segment.
     */
    private fun resolveFirstType(rawChainUnit: RawChainUnit): TypeDescriptor {
        val elements = rawChainUnit.myCallChain
        val firstElement = if (elements.isNotEmpty()) {
            elements.first() as? VariableNameMixin
        } else {
            null
        } ?: return TypeDescriptor.empty()

        val psiClass: PsiClass =
                // take predefined class
                rawChainUnit.myDeclaration?.resolutionResult?.psiClass
                        // try to find class from variables declared in current file
                        ?: FileVariablesResolver.resolveVariable(firstElement).psiClass
                        // try to find class from predefined context objects
                        ?: PredefinedVariables.resolveByIdentifier(firstElement.variableName(), firstElement)
                        ?: return TypeDescriptor.empty()

        return JavaPsiClassTypeDescriptor(psiClass)
    }

    /**
     * Create java chain segment
     */
    private fun constructJavaChainSegment(inputType: JavaPsiClassTypeDescriptor,
                                          rawChainUnit: RawChainUnit): CallChainSegment = chainSegment {
        this.inputType = inputType

        val rawElements = LinkedList(rawChainUnit.myCallChain)
        val result: ArrayList<CallChainElement> = ArrayList()

        var currentType = inputType
        var currentElement = rawElements.pop()

        var nextCallChainElement = when {
            rawElements.firstOrNull() is HtlArrayLikeAccess
                    && currentType.isList() -> {
                // create list element
                // consume next raw item
                // currentType = list value type
                TypeDescriptor.empty()
            }
            rawElements.firstOrNull() is HtlArrayLikeAccess
                    && currentType.isMap() -> {
                // create map element
                // consume next raw item
                // currentType = map value type
                TypeDescriptor.empty()
            }
            else -> {
                JavaPsiClassTypeDescriptor(currentType.psiClass)
            }
        }

        result += BaseChainElement(currentElement, extractElementName(currentElement),
                currentType)

        while (rawElements.isNotEmpty()) {
            currentElement = rawElements.pop()

            when {
                currentType is ListJavaTypeDescriptor
                        && rawElements.firstOrNull() is HtlArrayLikeAccess -> {

                }
                currentType is MapJavaTypeDescriptor
                        && rawElements.firstOrNull() is HtlArrayLikeAccess -> {

                }
            }
        }
    }

    /**
     * Current implementation resolves only one level of predefined
     * completion variants.
     */
    private fun constructPredefinedChainSegment(rawChainUnit: RawChainUnit): CallChainSegment
            = chainSegment {

        inputType = TypeDescriptor.empty()

        val variants = rawChainUnit
                .myDeclaration
                ?.resolutionResult
                ?.predefined
                ?: listOf()

        // we do not try to resolve current chain
        // the predefined set of variants is the resulting set
        outputType = PredefinedVariantsTypeDescriptor(variants)

        chain = listOf()
    }

    private fun extractElementName(nextField: PsiElement?): String {
        return when (nextField) {
            is AccessIdentifierMixin -> nextField.variableName()
            is VariableNameMixin -> nextField.variableName()
            else -> ""
        }
    }

}

