package com.aemtools.completion.html

import com.aemtools.completion.htl.completionprovider.SlyUseCompletionProvider
import com.aemtools.completion.util.elementType
import com.aemtools.completion.util.findParentByType
import com.aemtools.constant.const
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import com.intellij.util.ProcessingContext

/**
 * Provides available "data-sly-use" variants.
 * @author Dmytro_Troynikov
 */
object htmlDataSlyUseCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?,
                                result: CompletionResultSet) {
        if (result.isStopped
                || !accept(parameters)
                || parameters.originalPosition !is XmlToken) {
            return
        }

        if (isDataSlyUseValue(parameters)) {
            SlyUseCompletionProvider.addCompletionVariants(parameters, context, result)
        }
    }

    /**
     * Will return **true** for following input:
     *
     * _<div data-sly-use.beanName="&lt;caret&gt;"_
     */
    private fun isDataSlyUseValue(parameters: CompletionParameters): Boolean {
        val currentPosition = parameters.originalPosition
        val currentPositionType = currentPosition.elementType()

        if (currentPositionType == const.xml.XML_ATTRIBUTE_VALUE
                || currentPositionType == XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER.toString()) {
            val attribute = currentPosition.findParentByType(XmlAttribute::class.java) ?: return false

            return attribute.name.startsWith("data-sly-use")
        }

        return false
    }

    private fun accept(parameters: CompletionParameters): Boolean
            = parameters.originalFile.name.endsWith(".html")

}
