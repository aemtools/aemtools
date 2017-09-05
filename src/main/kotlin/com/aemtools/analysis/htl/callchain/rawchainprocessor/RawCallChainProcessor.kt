package com.aemtools.analysis.htl.callchain.rawchainprocessor

import com.aemtools.analysis.htl.callchain.elements.*
import com.aemtools.analysis.htl.callchain.elements.helper.chainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.base.EmptyTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.MergedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.predefined.PredefinedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ArrayJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.IterableJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateHolderTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateParameterTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.htl.model.declaration.*
import com.aemtools.completion.htl.predefined.HtlELPredefined.LIST_AND_REPEAT_HELPER_OBJECT
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
object RawCallChainProcessor {
    fun processChain(rawChain: LinkedList<RawChainUnit>): CallChain {
        if (rawChain.isEmpty()) {
            return CallChain.empty()
        }

        val segments = ArrayList<CallChainSegment>()

        val firstElement: RawChainUnit = rawChain.pop()

        var firstSegment = extractFirstSegment(firstElement)

        segments.add(firstSegment)

        while (rawChain.isNotEmpty()) {
            val outputType = firstSegment.outputType()
            val newSegment = when (outputType) {
                is JavaPsiClassTypeDescriptor ->
                    constructTypedChainSegment(outputType,
                            rawChain.pop())
                is TemplateTypeDescriptor ->
                    constructTypedChainSegment(outputType,
                            rawChain.pop())
                is TemplateHolderTypeDescriptor ->
                    constructTypedChainSegment(outputType,
                            rawChain.pop())

                else -> constructEmptyChainSegment(rawChain.pop())
            }

            segments.add(newSegment)
            firstSegment = newSegment
        }
        return CallChain(segments)
    }

    private fun extractFirstSegment(rawChainUnit: RawChainUnit): CallChainSegment {
        val declaration = rawChainUnit.myDeclaration

        val type: TypeDescriptor? = when (declaration) {
            is HtlUseVariableDeclaration -> {
                declaration.typeDescriptor()
            }
            is HtlTemplateDeclaration -> {
                TemplateTypeDescriptor(
                        declaration.templateDefinition,
                        rawChainUnit.myDeclaration.xmlAttribute.project)
            }
            is HtlTemplateParameterDeclaration -> {
                TemplateParameterTypeDescriptor(declaration)
            }
            is HtlListHelperDeclaration -> {
                PredefinedTypeDescriptor(LIST_AND_REPEAT_HELPER_OBJECT)
            }
            is HtlVariableDeclaration -> {
                TypeDescriptor.empty()
            }
            else -> null
        }

        if (type != null) {
            return constructTypedChainSegment(type, rawChainUnit)
        }

        val inputType = resolveFirstType(rawChainUnit)

        if (inputType.isEmpty()) {
            return createAttributeChainElement(rawChainUnit)
        }

        if (inputType is JavaPsiClassTypeDescriptor
                || inputType is MergedTypeDescriptor) {
            if (rawChainUnit.myCallChain.isNotEmpty()) {
                return constructTypedChainSegment(inputType, rawChainUnit)
            }
            return BaseCallChainSegment(inputType, inputType, rawChainUnit.myDeclaration, emptyList())
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

        var psiClass: PsiClass? = null

        if (psiClass == null && firstElement != null) {
            val type = PredefinedVariables.typeDescriptorByIdentifier(firstElement, firstElement.project)
            if (type !is EmptyTypeDescriptor) {
                return type
            }
        }

        if (psiClass == null) {
            val declaration = rawChainUnit.myDeclaration
            if (declaration is HtlUseVariableDeclaration) {
                return declaration.typeDescriptor()
            }
        }

        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass, null, null)
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
                    val typeDescriptor = JavaPsiClassTypeDescriptor(psiClass, null, null)
                    return BaseCallChainSegment(typeDescriptor, typeDescriptor, rawChainUnit.myDeclaration, listOf())
                }
            }
        }
        return CallChainSegment.empty()
    }

    private fun constructEmptyChainSegment(rawChainUnit: RawChainUnit)
            : CallChainSegment = chainSegment {
        this.inputType = inputType
        this.declarationType = rawChainUnit.myDeclaration

        val rawElements = LinkedList(rawChainUnit.myCallChain)
        val result: ArrayList<CallChainElement> = ArrayList()
        while (rawElements.isNotEmpty()) {
            val nextElement = rawElements.pop()
            val elementName = extractElementName(nextElement)

            result.add(BaseChainElement(nextElement, elementName, TypeDescriptor.Companion.named(elementName)))
        }

        chain = result

        this.outputType = result.last().type
    }

    /**
     * Create typed chain segment.
     */
    private fun constructTypedChainSegment(inputType: TypeDescriptor,
                                           rawChainUnit: RawChainUnit): CallChainSegment = chainSegment {
        this.inputType = inputType
        this.declarationType = rawChainUnit.myDeclaration
        val rawElements = LinkedList(rawChainUnit.myCallChain)
        val result: ArrayList<CallChainElement> = ArrayList()

        var currentType: TypeDescriptor = inputType
        var currentElement = rawElements.pop()

        var callChainElement = when {
            rawChainUnit.myDeclaration?.attributeType == DeclarationAttributeType.LIST_HELPER
                    || rawChainUnit.myDeclaration?.attributeType == DeclarationAttributeType.REPEAT_HELPER -> {
                BaseChainElement(currentElement,
                        extractElementName(currentElement),
                        PredefinedTypeDescriptor(LIST_AND_REPEAT_HELPER_OBJECT))
            }

            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && inputType is ArrayJavaTypeDescriptor -> {

                currentType = inputType.arrayType()
                BaseChainElement(currentElement, extractElementName(currentElement), currentType)
            }

            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && inputType is IterableJavaTypeDescriptor -> {

                currentType = inputType.iterableType()
                BaseChainElement(currentElement, extractElementName(currentElement), currentType)
            }

            rawChainUnit.myDeclaration?.type == DeclarationType.ITERABLE
                    && inputType is MapJavaTypeDescriptor -> {

                currentType = inputType.keyType()
                BaseChainElement(currentElement, extractElementName(currentElement), currentType)
            }

            else -> BaseChainElement(currentElement, extractElementName(currentElement), currentType)
        }

        result.add(callChainElement)

        while (rawElements.isNotEmpty()) {
            val nextRawElement = rawElements.pop()
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

    private fun extractElementName(element: PsiElement?): String {
        return when (element) {
            is AccessIdentifierMixin -> element.variableName()
            is VariableNameMixin -> element.variableName()
            else -> ""
        }
    }

}

