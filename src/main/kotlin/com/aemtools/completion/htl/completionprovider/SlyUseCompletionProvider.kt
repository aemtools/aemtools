package com.aemtools.completion.htl.completionprovider

import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiModifier
import com.intellij.util.ProcessingContext

/**
 * Code completion for __data-sly-use.*__ attribute. (e.g. <div data-sly-use.bean="<caret>")
 * Collects all classes in project which do implement
 * @author Dmytro Troynikov.
 */
object SlyUseCompletionProvider : CompletionProvider<CompletionParameters>() {

    fun useSuggestions(parameters: CompletionParameters): List<LookupElement> {
        val project = parameters.position.project

        val useClasses = JavaSearch.findWcmUseClasses(project)
        val slingModelClasses = JavaSearch.findSlingModels(project)
        val classes = useClasses + slingModelClasses

        return classes
                .filter { !it.hasModifierProperty(PsiModifier.ABSTRACT) }
                .map {
                    LookupElementBuilder.create(it.qualifiedName as String)
                            .withLookupString(it.name as String)
                }
    }

    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        result.addAllElements(useSuggestions(parameters))
    }

}