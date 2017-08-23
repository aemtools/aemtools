package com.aemtools.completion.htl.provider

import com.aemtools.index.HtlIndexFacade
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlI18NKeyCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val position = parameters.position

        val localizations = HtlIndexFacade.getAllLocalizationModels(position.project)

        result.addAllElements(localizations.map {
            LookupElementBuilder.create(it.key)
        })

        result.stopHere()
    }

}