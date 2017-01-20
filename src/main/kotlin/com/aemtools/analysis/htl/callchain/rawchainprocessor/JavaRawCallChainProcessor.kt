package com.aemtools.analysis.htl.callchain.rawchainprocessor

import com.aemtools.analysis.htl.callchain.elements.BaseChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChainSegment
import com.aemtools.analysis.htl.callchain.elements.helper.chainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.PredefinedVariantsTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ListJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.completionprovider.PredefinedVariables
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.lang.htl.psi.HtlArrayLikeAccess
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.java.JavaSearch
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

        if (inputType.isEmpty()) {

        }

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
        }

        var psiClass: PsiClass? = rawChainUnit.myDeclaration?.resolutionResult?.psiClass

        if (psiClass == null && firstElement != null) {
            psiClass = FileVariablesResolver.resolveVariable(firstElement).psiClass
        }

        if (psiClass == null && firstElement != null) {
            psiClass = PredefinedVariables.resolveByIdentifier(firstElement.variableName(), firstElement.project)
        }

        // in that case the new chain segment should be created here
        if (psiClass == null) {
            val attribute = rawChainUnit.myDeclaration?.xmlAttribute
            if (attribute != null) {
                val className = rawChainUnit.myDeclaration?.xmlAttribute?.resolveUseClass()
                if (className != null) {
                    psiClass = JavaSearch.findClass(className, attribute.project)
                }
            }
        }

        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass)
        } else {
            TypeDescriptor.empty()
        }
    }

    private fun createAttributeChainElement(rawChainUnit: RawChainUnit): CallChainSegment {
        val xmlAttribute = rawChainUnit.myDeclaration?.xmlAttribute
        if (xmlAttribute != null) {
            val className = xmlAttribute.resolveUseClass()
            if (className != null) {
                val psiClass = JavaSearch.findClass(className, xmlAttribute.project)
                if (psiClass != null) {
                    return
                }
            }
        }
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

