package com.aemtools.lang.el.editor

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class ElBackspaceHandler : BackspaceHandlerDelegate() {

  override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {
  }

  override fun charDeleted(c: Char, file: PsiFile, editor: Editor): Boolean {
    if (!ElEditorActionUtil.isInElExpression(file, editor)) {
      return false
    }

    val offset = editor.caretModel.offset
    val document = editor.document
    val nextChar = document.charsSequence.getOrNull(offset) ?: return false
    val expectedPair = when (c) {
      '(' -> ')'
      '[' -> ']'
      '\'' -> '\''
      else -> return false
    }

    if (nextChar != expectedPair) {
      return false
    }

    document.deleteString(offset, offset + 1)
    return true
  }

}
