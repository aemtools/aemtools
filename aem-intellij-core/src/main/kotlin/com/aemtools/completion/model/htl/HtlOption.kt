package com.aemtools.completion.model.htl

import com.aemtools.common.completion.lookupElement
import com.aemtools.completion.htl.inserthandler.HtlElArrayOptionInsertHandler
import com.aemtools.completion.htl.inserthandler.HtlElStringOptionInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.icons.AllIcons

/**
 * @author Dmytro Primshyts
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
  fun toLookupElement(): LookupElement = lookupElement(name)
      .withTypeText("HTL Option")
      .withIcon(AllIcons.Nodes.Parameter)
      .withInsertHandler(when (name) {
        "format" -> HtlElArrayOptionInsertHandler()
        "i18n" -> null
        else -> HtlElStringOptionInsertHandler()
      })

}
