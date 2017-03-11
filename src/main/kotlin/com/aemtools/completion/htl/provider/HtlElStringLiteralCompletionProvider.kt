package com.aemtools.completion.htl.provider

import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isMainString
import com.aemtools.constant.const
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.lang.StdLanguages
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlElStringLiteralCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val currentPosition = parameters.position.findParentByType(HtlStringLiteral::class.java)
                ?: return
        val psi = currentPosition.containingFile.viewProvider.getPsi(StdLanguages.HTML)
        val attributes = PsiTreeUtil.findChildrenOfType(psi, XmlAttribute::class.java)

        // find the XmlAttribute which contains currentPosition
        val containerAttribute = attributes.find { it ->
            val valueElement = it.valueElement
            if (valueElement == null) {
                false
            } else {
                val valueOffset = valueElement.textOffset
                val valueLength = valueElement.textLength

                return@find valueOffset < currentPosition.textOffset
                        && currentPosition.textOffset < valueOffset + valueLength
            }
        } ?: return

        // data-sly-use.bean="${'<caret>'}"
        if (containerAttribute.name.startsWith(const.htl.DATA_SLY_USE)
                && currentPosition.isMainString()) {
            result.addAllElements(SlyUseCompletionProvider.useSuggestions(parameters))
        }

        result.stopHere()
    }

}