package com.aemtools.completion.htl.provider

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.findParentByType
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlI18NKeyCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext,
                              result: CompletionResultSet) {
    val position = parameters.position

    if (localizationMainString(position)) {
      val localizations = HtlIndexFacade.getAllLocalizationModels(position.project)

      result.addAllElements(localizations.map {
        LookupElementBuilder.create(it.key)
            .withTypeText("i18n", true)
      })

      result.stopHere()
    }
  }

  private fun localizationMainString(position: PsiElement): Boolean {
    return position.findParentByType(HtlHtlEl::class.java)
        ?.findChildrenByType(PsiElement::class.java)
        ?.any { it.text == "i18n" }
        ?: false
  }

}
