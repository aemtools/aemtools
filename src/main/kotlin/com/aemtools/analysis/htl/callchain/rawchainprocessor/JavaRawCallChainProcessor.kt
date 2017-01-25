package com.aemtools.analysis.htl.callchain.rawchainprocessor

import com.aemtools.analysis.htl.callchain.elements.*
import com.aemtools.analysis.htl.callchain.elements.helper.chainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.PredefinedVariantsTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ArrayJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.IterableJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.completionprovider.PredefinedVariables
import com.aemtools.completion.htl.model.DeclarationType
import com.aemtools.completion.htl.predefined.HtlELPredefined.DATA_SLY_LIST_REPEAT_LIST_FIELDS
import com.aemtools.completion.util.hasChild
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

        val firstElement: RawChainUnit = rawChain.pop()

        var firstSegment = extractFirstSegment(firstElement)

        segments.add(firstSegment)

        while (rawChain.isNotEmpty() && firstSegment.outputType() is JavaPsiClassTypeDescriptor) {
            val newSegment = constructJavaChainSegment(firstSegment.outputType() as JavaPsiClassTypeDescriptor,
                    segments.lastOrNull() as BaseCallChainSegment?,
                    rawChain.pop())
            segments.add(newSegment)
            firstSegment = newSegment
        }
        return CallChain(segments)
    }

    private fun extractFirstSegment(rawChainUnit: RawChainUnit): CallChainSegment {
        if (rawChainUnit.hasPredefinedVariants()) {
            return constructPredefinedChainSegment(rawChainUnit)
        }

        if (rawChainUnit.myDeclaration?.resolutionResult?.psiClass != null) {
            return createAttributeChainElement(rawChainUnit)
        }

        val inputType = resolveFirstType(rawChainUnit)

        var chainSegment: CallChainSegment?

        if (inputType.isEmpty()) {
            chainSegment = createAttributeChainElement(rawChainUnit)
            return chainSegment
        }

        if (inputType is JavaPsiClassTypeDescriptor) {
            return constructJavaChainSegment(inputType, null, rawChainUnit)
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

        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass, null)
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
                    val typeDescriptor = JavaPsiClassTypeDescriptor(psiClass, null)
                    return BaseCallChainSegment(typeDescriptor, typeDescriptor, rawChainUnit.myDeclaration, listOf())
                }
            }
        }
        return CallChainSegment.empty()
    }

    /**
     * Create java chain segment
     */
    private fun constructJavaChainSegment(inputType: JavaPsiClassTypeDescriptor,
                                          previousSegment: BaseCallChainSegment?,
                                          rawChainUnit: RawChainUnit): CallChainSegment = chainSegment {
        this.inputType = inputType
        this.declarationType = rawChainUnit.myDeclaration
        val rawElements = LinkedList(rawChainUnit.myCallChain)
        val result: ArrayList<CallChainElement> = ArrayList()

        var currentType: TypeDescriptor = inputType
        var currentElement = rawElements.pop()

        var callChainElement = when {
            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && extractElementName(currentElement).endsWith("List") ->
                BaseChainElement(currentElement,
                        extractElementName(currentElement),
                        PredefinedVariantsTypeDescriptor(DATA_SLY_LIST_REPEAT_LIST_FIELDS))
            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && inputType is ArrayJavaTypeDescriptor ->
                BaseChainElement(currentElement, extractElementName(currentElement), inputType.arrayType())
            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && inputType is IterableJavaTypeDescriptor ->
                BaseChainElement(currentElement, extractElementName(currentElement), inputType.iterableType())
            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && inputType is MapJavaTypeDescriptor ->
                BaseChainElement(currentElement, extractElementName(currentElement), inputType.keyType())
            else -> BaseChainElement(currentElement, extractElementName(currentElement), currentType)
        }

        result.add(callChainElement)

        while (rawElements.isNotEmpty()) {
            val nextRawElement = rawElements.pop()
            val varName = extractElementName(nextRawElement)

            when {
                currentType.isArray()
                        && currentType is ArrayJavaTypeDescriptor
                        && nextRawElement.hasChild(HtlArrayLikeAccess::class.java) -> {
                    callChainElement = ArrayAccessIdentifierElement(nextRawElement)
                    currentType = currentType.arrayType()
                }
                currentType.isIterable()
                        && currentType is IterableJavaTypeDescriptor
                        && nextRawElement.hasChild(HtlArrayLikeAccess::class.java) -> {
                    callChainElement = ArrayAccessIdentifierElement(nextRawElement)
                    currentType = currentType.iterableType()
                }
                currentType.isMap()
                        && currentType is MapJavaTypeDescriptor
                        && nextRawElement.hasChild(HtlArrayLikeAccess::class.java) -> {
                    callChainElement = ArrayAccessIdentifierElement(nextRawElement)
                    currentType = currentType.valueType()
                }
                else -> {
                    val varName = extractElementName(nextRawElement)
                    val newType = currentType.subtype(varName)
                    callChainElement = BaseChainElement(nextRawElement, varName, newType)
                    currentType = newType
                }
            }
            result.add(callChainElement)
        }

        chain = result

        outputType = result.last().type
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

        chain = rawChainUnit.myCallChain.map {
            BaseChainElement(it, extractElementName(it), outputType)
        }
    }

    private fun extractElementName(nextField: PsiElement?): String {
        return when (nextField) {
            is AccessIdentifierMixin -> nextField.variableName()
            is VariableNameMixin -> nextField.variableName()
            else -> ""
        }
    }

}

