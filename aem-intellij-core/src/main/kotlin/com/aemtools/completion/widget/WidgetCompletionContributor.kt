package com.aemtools.completion.widget

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.common.constant.const.file_names.DIALOG_XML
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts.
 */
class WidgetCompletionContributor : BaseCompletionContributor({

  basic(PlatformPatterns.psiElement(), WidgetCompletionProvider())

})

private class WidgetCompletionProvider : CompletionProvider<CompletionParameters>() {

  override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext,
                              result: CompletionResultSet) {
    if (!accept(parameters)) {
      return
    }

    val widgetDefinition = WidgetDefinitionUtil.extract(parameters.position)

    val suggestions = WidgetVariantsProvider.generateVariants(parameters, widgetDefinition)

    result.addAllElements(suggestions)
    result.stopHere()
  }

  private fun accept(parameters: CompletionParameters): Boolean {
    return DIALOG_XML == parameters.originalFile.name
  }

}

