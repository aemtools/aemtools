package com.aemtools.completion.htl.provider

import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.htl.model.declaration.DeclarationAttributeType
import com.aemtools.util.withPriority
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlElVariableNameCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val currentPosition = parameters.position
        val contextObjects = PredefinedVariables.contextObjectsCompletion()
                .map { it.withPriority(0.0) }

        val fileVariables = FileVariablesResolver.declarationsForPosition(parameters.position, parameters)
                .filter { it.attributeType != DeclarationAttributeType.DATA_SLY_TEMPLATE }
                .map { it.toLookupElement().withPriority(100000.0 - (currentPosition.textOffset - it.xmlAttribute.textOffset)) }
        result.addAllElements(fileVariables + contextObjects)
        result.stopHere()
    }

}