package com.aemtools.completion.htl.provider

import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isInsideOf
import com.aemtools.constant.const.htl.DATA_SLY_CALL
import com.aemtools.index.search.HtlTemplateSearch
import com.aemtools.lang.htl.psi.HtlHel
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElementWeigher
import com.intellij.psi.PsiElement
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

        val position = parameters.position

        if (position.isInsideOf(DATA_SLY_CALL)) {
            val templates = HtlTemplateSearch.all(position.project)

            val lookupElements = templates.map {
                LookupElementBuilder.create(it.name)
            }
            result.addAllElements(lookupElements)
            result.stopHere()
            return
        }

        val contextObjects = PredefinedVariables.contextObjectsCompletion()
        val fileVariables = FileVariablesResolver.findForPosition(parameters.position, parameters)
        result.addAllElements(contextObjects + fileVariables)
        result.stopHere()
    }

}