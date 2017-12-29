package com.aemtools.completion.model.htl

import com.aemtools.completion.htl.inserthandler.HtlElArrayOptionInsertHandler
import com.aemtools.completion.htl.inserthandler.HtlElStringOptionInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons

/**
 * @author Dmytro Troynikov
 */
data class HtlOption(val name: String,
                     val type: String,
                     val description: String,
                     val examples: List<String>,
                     val link: String) {

  /**
   * Convert current [HtlOption] into [LookupElement].
   *
   * @return new lookup element
   */
  fun toLookupElement(): LookupElement = LookupElementBuilder.create(name)
      .withTypeText("HTL Option")
      .withIcon(AllIcons.Nodes.Parameter)
      .withInsertHandler(when (name) {
        "format" -> HtlElArrayOptionInsertHandler()
        "i18n" -> null
        else -> HtlElStringOptionInsertHandler()
      })

}
