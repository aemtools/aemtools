package com.aemtools.completion.htl.provider

import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtlI18NKeyCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext,
                              result: CompletionResultSet) {
    val position = parameters.position

    if (localizationMainString(position)) {
      val localizations = HtlIndexFacade.getAllLocalizationKeys(position.project)

      result.addAllElements(localizations.map {
        lookupElement(it)
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
