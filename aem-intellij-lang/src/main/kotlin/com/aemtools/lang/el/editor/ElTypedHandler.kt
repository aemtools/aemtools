package com.aemtools.lang.el.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class ElTypedHandler : TypedHandlerDelegate() {

  override fun beforeCharTyped(
      c: Char,
      project: Project,
      editor: Editor,
      file: PsiFile,
      fileType: FileType
  ): Result {
    if (!ElEditorActionUtil.isInElExpression(file, editor)) {
      return Result.CONTINUE
    }

    val offset = editor.caretModel.offset
    val document = editor.document
    val nextChar = document.charsSequence.getOrNull(offset)

    if (c == '\'' && nextChar == c) {
      editor.caretModel.moveToOffset(offset + 1)
      return Result.STOP
    }

    val closingPair = when (c) {
      '(' -> ')'
      '[' -> ']'
      '\'' -> '\''
      else -> null
    }

    if (closingPair != null) {
      document.insertString(offset, "$c$closingPair")
      editor.caretModel.moveToOffset(offset + 1)
      return Result.STOP
    }

    if ((c == ')' || c == ']') && nextChar == c) {
      editor.caretModel.moveToOffset(offset + 1)
      return Result.STOP
    }

    return Result.CONTINUE
  }

}
