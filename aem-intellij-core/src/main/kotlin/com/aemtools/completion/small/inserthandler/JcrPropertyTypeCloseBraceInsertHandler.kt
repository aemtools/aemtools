package com.aemtools.completion.small.inserthandler

import com.aemtools.common.util.hasText
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement

/**
 * Inserts right brace after JCT Type completion.
 *
 * @author Kostiantyn Diachenko
 */
class JcrPropertyTypeCloseBraceInsertHandler : InsertHandler<LookupElement> {
  override fun handleInsert(context: InsertionContext, item: LookupElement) {
    val document = context.document
    val editor = context.editor
    val position = editor.caretModel.offset

    if (!document.hasText("}", position)) {
      document.insertString(position, "}")
      editor.caretModel.moveToOffset(position + 1)
    }
  }
}
