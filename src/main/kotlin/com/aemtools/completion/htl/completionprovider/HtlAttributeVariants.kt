package com.aemtools.completion.htl.completionprovider

import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object HtlAttributeVariants : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?,
                                result: CompletionResultSet) : Unit{
        result.addAllElements(repository.getAttributesData().map { LookupElementBuilder.create(it.name) })
    }

    private val repository = ServiceFacade.getHtlAttributesRepository()

}