package com.aemtools.lang.htl.editor

import com.aemtools.common.constant.const.DOLLAR
import com.aemtools.lang.util.isHtlFile
import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

/**
 * @author Dmytro Primshyts
 */
class HtlBackspaceHandler : BackspaceHandlerDelegate() {
  override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {
  }

  override fun charDeleted(c: Char, file: PsiFile, editor: Editor): Boolean {
    if (!file.isHtlFile()) {
      return false
    }

    if (c == '{') {
      val offset = editor.caretModel.offset
      if (offset < 1 || offset >= editor.document.textLength) {
        return false
      }

      val document = editor.document
      val nextChar = document.charsSequence[offset]
      val prevChar = document.charsSequence[offset - 1]

      if (nextChar == '}' && prevChar == DOLLAR[0]) {
        document.replaceString(offset - 1, offset + 1, "")
        return true
      }

    }

    if (c == '\'') {
      val offset = editor.caretModel.offset
      if (offset < 0 || offset >= editor.document.textLength) {
        return false
      }

      val document = editor.document
      val nextChar = document.charsSequence[offset]

      if (nextChar == '\'') {
        document.replaceString(offset, offset + 1, "")
        return true
      }
    }

    if (c == '"') {
      val offset = editor.caretModel.offset
      if (offset < 0 || offset >= editor.document.textLength) {
        return false
      }

      val document = editor.document
      val nextChar = document.charsSequence[offset]

      if (nextChar == '"') {
        document.replaceString(offset, offset + 1, "")
        return true
      }
    }
    return false
  }
}
