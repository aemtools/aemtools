package com.aemtools.completion.htl.provider

import com.aemtools.completion.htl.inserthandler.HtlElArrayOptionInsertHandler
import com.aemtools.completion.htl.inserthandler.HtlElStringOptionInsertHandler
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isInsideOF
import com.aemtools.constant.const.htl.DATA_SLY_CALL
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
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
        val hel = currentPosition.findParentByType(HtlHtlEl::class.java)
                ?: return
        if (hel.isInsideOF(DATA_SLY_TEMPLATE)) {
            return
        }

        if (hel.isInsideOF(DATA_SLY_CALL)) {
            val myPropertyChain = hel.findChildrenByType(PropertyAccessMixin::class.java)
                    .firstOrNull() ?: return

            val accessChain = myPropertyChain.accessChain()
        }

        var children = hel.findChildrenByType(HtlContextExpression::class.java)
        if (children != null) {
            val parent = currentPosition.findParentByType(HtlContextExpression::class.java)
            if (parent != null) {
                children -= parent
            }
        }
        val completionVariabts = CONTEXT_PARAMETERS
                .filter {
                    if (children.isEmpty()) {
                        true
                    } else {
                        children.forEach { child ->
                            val assignment = child.assignment
                            if (assignment != null) {
                                if (assignment.variableName.varName.text == it) {
                                    return@filter false
                                }
                            } else {
                                val variableName = child.variableName
                                if (variableName != null) {
                                    if (variableName.varName.text == it) {
                                        return@filter false
                                    }
                                }
                            }
                        }
                        return@filter true
                    }
                }
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

        result.addAllElements(completionVariabts)
    }

}