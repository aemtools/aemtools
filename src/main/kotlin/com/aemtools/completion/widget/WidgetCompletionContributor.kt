package com.aemtools.completion.widget

import com.aemtools.completion.util.WidgetDefinitionUtil
import com.aemtools.constant.const
import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov.
 */
class WidgetCompletionContributor : CompletionContributor {

    constructor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), WidgetCompletionProvider())
    }

}

private class WidgetCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext,
                                result: CompletionResultSet) {
        if (!accept(parameters)) {
            return
        }

        val widgetDefinition = WidgetDefinitionUtil.extract(parameters.position)

        val suggestions = WidgetVariantsProvider.Companion.INSTANCE()
                .generateVariants(parameters, widgetDefinition)

        result.addAllElements(suggestions)
        result.stopHere()
    }

    private fun accept(parameters: CompletionParameters): Boolean {
        return const.DIALOG_XML == parameters.originalFile.name
    }

}


