package com.aemtools.completion.htl.completionprovider

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * @author Dmytro Troynikov
 */
object HtlContextCompletionProvider {

    val CONTEXT_PARAMETERS = listOf("join",
            "i18n", "context", "format", "locale",
            "type", "timezone", "scheme", "domain",
            "path", "prependPath", "appendPath",
            "selectors", "addSelectors", "removeSelectors",
            "extension", "suffix", "prependSuffix", "appendSuffix",
            "query", "addQuery", "removeQuery", "fragment")

    fun contextParameters(parameters: CompletionParameters): List<LookupElement> {
        val currentPosition = parameters.position
        val hel = currentPosition.findParentByType(HtlHtlEl::class.java)
        var children = hel.findChildrenByType(HtlContextExpression::class.java)
        if (children != null) {
            val parent = currentPosition.findParentByType(HtlContextExpression::class.java)
            if (parent != null) {
                children -= parent
            }
        }
        return CONTEXT_PARAMETERS
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
                .map { LookupElementBuilder.create(it) }
    }

}