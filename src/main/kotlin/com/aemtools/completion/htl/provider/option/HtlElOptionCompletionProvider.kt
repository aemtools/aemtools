package com.aemtools.completion.htl.provider.option

import com.aemtools.completion.htl.inserthandler.HtlElArrayOptionInsertHandler
import com.aemtools.completion.htl.inserthandler.HtlElStringOptionInsertHandler
import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlElOptionCompletionProvider : CompletionProvider<CompletionParameters>() {

    val CONTEXT_PARAMETERS = listOf("join",
            "i18n", "context", "format", "locale",
            "timezone", "scheme", "domain",
            "path", "prependPath", "appendPath",
            "selectors", "addSelectors", "removeSelectors",
            "extension", "suffix", "prependSuffix", "appendSuffix",
            "query", "addQuery", "removeQuery", "fragment")

    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        val currentPosition = parameters.position
        val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
                ?: return

        val names = hel.getOptions().map { it.name() }
                .filterNot { it == "" }

        val completionVariants = CONTEXT_PARAMETERS
                .filterNot { names.contains(it) }
                .map {
                    LookupElementBuilder.create(it)
                            .withTypeText("HTL Option")
                            .withIcon(AllIcons.Nodes.Parameter)
                            .withInsertHandler(when (it) {
                                "format" -> HtlElArrayOptionInsertHandler()
                                "i18n" -> null
                                else -> HtlElStringOptionInsertHandler()
                            })
                }

        result.addAllElements(completionVariants)
    }

}