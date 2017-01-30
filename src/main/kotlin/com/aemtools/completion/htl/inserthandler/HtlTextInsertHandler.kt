package com.aemtools.completion.htl.inserthandler

import com.aemtools.completion.util.hasText
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.editor.Document

abstract class HtlTextInsertHandler(private val expression: String,
                                    private val offset: Int) : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext?, item: LookupElement?) {
        val document = context?.document ?: return
        val editor = context?.editor ?: return
        val position = editor.caretModel.offset

        if (!tagHasExpression(document, position)) {
            document.insertString(position, expression)
            editor.caretModel.moveToOffset(position + offset)
        }
    }

    private fun tagHasExpression(document: Document, position: Int) =
            document.hasText("='" + expression, position) || document.hasText("=\"" + expression, position)

}
