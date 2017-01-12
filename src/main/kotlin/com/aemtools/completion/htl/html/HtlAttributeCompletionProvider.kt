package com.aemtools.completion.htl.html

import com.aemtools.constant.const
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

/**
 * Provider of Htl specific attributes.
 * @author Dmytro_Troynikov
 */
object HtlAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        result.addAllElements(vars)
    }

    private val vars: List<LookupElement> = const.htl.HTL_ATTRIBUTES.map {
        val result = LookupElementBuilder.create(it)
                .withTypeText("HTL Attribute")
        if (it == DATA_SLY_UNWRAP) {
            result
        } else {
            result.withInsertHandler(XmlAttributeInsertHandler())
        }
    }

}
