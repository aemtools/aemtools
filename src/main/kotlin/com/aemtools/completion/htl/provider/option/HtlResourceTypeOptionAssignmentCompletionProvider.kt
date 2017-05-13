package com.aemtools.completion.htl.provider.option

import com.aemtools.index.model.AemComponentDefinition.Companion.toLookupElement
import com.aemtools.index.search.AemComponentSearch
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.util.ProcessingContext
import org.apache.commons.lang.StringUtils

/**
 * @author Dmytro Troynikov
 */
object HtlResourceTypeOptionAssignmentCompletionProvider
    : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext?,
            result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val myDirectory = parameters.position.containingFile.originalFile.containingDirectory.virtualFile
                .path

        val declarations = AemComponentSearch
                .allComponentDeclarations(parameters.position.project)
                .map {
                    val lookupElement = it.toLookupElement()

                    PrioritizedLookupElement
                            .withPriority(lookupElement, calcPriority(lookupElement, myDirectory))
                }
        result.addAllElements(declarations)
        result.stopHere()
    }

    private fun calcPriority(lookupElement: LookupElement, myDirectory: String): Double {
        return 1 - StringUtils.getLevenshteinDistance(lookupElement.lookupString, myDirectory)
                .toDouble() / 100
    }

}