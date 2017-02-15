package com.aemtools.completion.htl

import com.aemtools.analysis.htl.callchain.elements.resolveSelectedItem
import com.aemtools.analysis.htl.callchain.elements.selectedElement
import com.aemtools.completion.htl.completionprovider.PredefinedEL
import com.aemtools.completion.htl.completionprovider.PredefinedVariables
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.htl.predefined.HtlELPredefined
import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.util.ProcessingContext
import generated.psi.impl.HtlPropertyAccessImpl

/**
 * @author Dmytro_Troynikov
 */
object HtlELCompletionProvider : CompletionProvider<CompletionParameters>() {
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

            else -> return
        }
        result.stopHere()
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

        val chain = propertyAccessElement.accessChain()
                ?: return ResolutionResult()
        val lastSegment = chain.callChainSegments.lastOrNull()
                ?: return ResolutionResult()
        val selectedElement = lastSegment.selectedElement()
                ?: return ResolutionResult()

        val result = lastSegment.resolveSelectedItem()

        return HtlELPredefined.addPredefined(chain, lastSegment, selectedElement, result)
    }

    /**
     * Will return __true__ if current user accesses to the element of some class.
     */
    fun isMemberAccess(element: PsiElement): Boolean {
        val parent = element.findParentByType(HtlPropertyAccessImpl::class.java) ?: return false
        return parent.children.size != 1
    }

}