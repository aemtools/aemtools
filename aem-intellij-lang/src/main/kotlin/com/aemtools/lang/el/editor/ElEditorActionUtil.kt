package com.aemtools.lang.el.editor

import com.aemtools.lang.el.ElLanguage
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue

/**
 * Helpers for editor actions in EL fragments embedded into XML attributes.
 */
object ElEditorActionUtil {

  fun isInElExpression(file: PsiFile, editor: Editor): Boolean {
    val text = editor.document.charsSequence
    val offset = editor.caretModel.offset
    if (file.language == ElLanguage) {
      return isInElExpression(text, offset, 0, text.length)
    }

    if (file.name != ".content.xml") {
      return false
    }

    val attributeValue = attributeValueAt(file, offset) ?: return false
    val range = attributeValue.textRange
    if (offset <= range.startOffset || offset >= range.endOffset) {
      return false
    }

    return isInElExpression(text, offset, range.startOffset + 1, range.endOffset - 1)
  }

  private fun isInElExpression(
      text: CharSequence,
      offset: Int,
      start: Int,
      end: Int
  ): Boolean {
    if (offset !in (start + 1)..<end) {
      return false
    }

    var position = start
    while (position < end) {
      val expressionStart = findNextExpressionStart(text, position, end)
          ?: return false
      val expressionEnd = findExpressionEnd(text, expressionStart, end)
          ?: return offset > expressionStart + 1

      if (offset > expressionStart + 1 && offset <= expressionEnd) {
        return true
      }

      position = expressionEnd + 1
    }

    return false
  }

  private fun attributeValueAt(file: PsiFile, offset: Int): XmlAttributeValue? {
    val candidates = listOf(offset, offset - 1)
    for (candidate in candidates) {
      if (candidate < 0 || candidate >= file.textLength) {
        continue
      }

      val element = file.findElementAt(candidate) ?: continue
      PsiTreeUtil.getParentOfType(element, XmlAttributeValue::class.java, false)?.let {
        return it
      }
    }

    return null
  }

  private fun findNextExpressionStart(text: CharSequence, start: Int, end: Int): Int? {
    var position = start
    while (position + 1 < end) {
      if ((text[position] == '$' || text[position] == '#') && text[position + 1] == '{') {
        return position
      }
      position++
    }
    return null
  }

  private fun findExpressionEnd(text: CharSequence, start: Int, end: Int): Int? {
    var openBraces = 0
    var quote: Char? = null
    var position = start

    while (position < end) {
      val char = text[position]

      if ((char == '\'' || char == '"') && (position == start || text[position - 1] != '\\')) {
        quote = if (quote == char) null else quote ?: char
      }

      if (quote == null) {
        when (char) {
          '{' -> openBraces++
          '}' -> {
            openBraces--
            if (openBraces == 0) {
              return position
            }
          }
        }
      }

      position++
    }

    return null
  }

}
