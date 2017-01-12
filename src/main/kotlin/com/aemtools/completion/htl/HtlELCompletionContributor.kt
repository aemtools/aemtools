package com.aemtools.completion.htl

import com.aemtools.completion.htl.completionprovider.*
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.htl.predefined.HtlELPredefined
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.hasParent
import com.aemtools.completion.util.isMainString
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.lang.htl.psi.HtlAssignment
import com.aemtools.lang.htl.psi.HtlAssignmentValue
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.StdLanguages
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext
import generated.psi.impl.HtlPropertyAccessImpl

/**
 * @author Dmytro Troynikov.
 */
class HtlELCompletionContributor : CompletionContributor {
    constructor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), HtlELCompletionProvider())
    }
}

private class HtlELCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val currentPosition = parameters.position
        when {
            currentPosition is OuterLanguageElement -> return
            // ${object.<caret>}
            isMemberAccess(currentPosition) -> {
                val resolutionResult = resolveClass(currentPosition, parameters, context)

                when {
                    resolutionResult.predefined != null -> {
                        result.addAllElements(resolutionResult.predefined)
                    }
                    resolutionResult.psiClass != null -> {
                        // TODO: add unit test for different merge modes ("merge", "override", "disabled")
                        val predefined = PredefinedEL.predefinedCompletions(parameters, context, result)
                        val completions = PredefinedVariables.extractSuggestions(resolutionResult.psiClass)
                        val merged = mergeCompletions(completions, predefined.first, predefined.second)
                        result.addAllElements(merged)
                    }
                    else -> {
                        // unable to resolve current element, doing nothing
                    }
                }
            }

            isAssignment(currentPosition) -> {
                if (currentPosition.findParentByType(HtlContextExpression::class.java) != null) {
                    // ${param @ opt='<caret>'}
                    if (currentPosition.findParentByType(HtlAssignmentValue::class.java) != null) {
                        val sightlyAssignment = currentPosition.findParentByType(HtlAssignment::class.java) ?: return
                        val variableElement = sightlyAssignment.firstChild
                        if (variableElement.text == "context") {
                            result.addAllElements(HtlELPredefined.CONTEXT_VALUES.map {
                                LookupElementBuilder.create(it.completionText)
                            })
                        }
                    }
                }
            }
            isOption(parameters) -> {
                result.addAllElements(HtlContextCompletionProvider.contextParameters(parameters))
            }
            isStringLiteralValue(parameters) -> {
                val values = completeStringLiteralValue(parameters)
                result.addAllElements(values)
            }
            isVariable(parameters) -> {
                val contextObjects = PredefinedVariables.contextObjectsCompletion()
                val fileCompletions = FileVariablesResolver
                        .findForPosition(currentPosition, parameters)
                result.addAllElements(contextObjects + fileCompletions)
            }

            else -> return
        }
        result.stopHere()
    }

    fun completeStringLiteralValue(parameters: CompletionParameters): List<LookupElement> {
        val currentPosition = parameters.position.findParentByType(HtlStringLiteral::class.java)
                ?: return listOf()
        val psi = currentPosition.containingFile.viewProvider.getPsi(StdLanguages.HTML)
        val attributes = PsiTreeUtil.findChildrenOfType(psi, XmlAttribute::class.java)

        // find the XmlAttribute which contains currentPosition
        val containerAttribute = attributes.find { it ->
            val valueElement = it.valueElement
            if (valueElement == null) {
                false
            } else {
                val valueOffset = valueElement.textOffset
                val valueLength = valueElement.textLength

                return@find valueOffset < currentPosition.textOffset
                        && currentPosition.textOffset < valueOffset + valueLength
            }
        } ?: return listOf()

        // data-sly-use.bean="${'<caret>'}"
        if (containerAttribute.name.startsWith(DATA_SLY_USE)
                && currentPosition.isMainString()) {
            return SlyUseCompletionProvider.useSuggestions(parameters)
        }

        return listOf()
    }

    private fun mergeCompletions(first: List<LookupElement>, second: List<LookupElement>, mode: String): List<LookupElement> {
        return when (mode) {
            "merge" -> first + second
            "override" -> second
            else -> first // mode == "disabled" by default
        }
    }

    fun resolveClass(element: PsiElement,
                     parameters: CompletionParameters,
                     context: ProcessingContext): ResolutionResult {
        val propertyAccessElement = element.findParentByType(PropertyAccessMixin::class.java)
                ?: return ResolutionResult()

        val propertyAccessChain = propertyAccessElement.accessChain()
        context.put("property-access-chain", propertyAccessChain)
        return propertyAccessChain.last().resolutionResult
    }

    /**
     * Will return __true__ if current user accesses to the element of some class.
     */
    fun isMemberAccess(element: PsiElement): Boolean {
        val parent = element.findParentByType(HtlPropertyAccessImpl::class.java) ?: return false
        return parent.children.size != 1
    }

    /**
     * Will return `true` for following structure:
     *
     * ${param @ option='<caret>'}
     */
    fun isAssignment(element: PsiElement): Boolean {
        return element.hasParent(HtlAssignment::class.java)
    }

    fun isOption(parameters: CompletionParameters): Boolean {
        return parameters.position.hasParent(HtlContextExpression::class.java)
    }

    /**
     * Check if current position is value of literal ('<caret>')
     */
    fun isStringLiteralValue(parameters: CompletionParameters): Boolean
            = parameters.position.hasParent(HtlStringLiteral::class.java)

    fun isVariable(parameters: CompletionParameters): Boolean {
        return true
    }

}