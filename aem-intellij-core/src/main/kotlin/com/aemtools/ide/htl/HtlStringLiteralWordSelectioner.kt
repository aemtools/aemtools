package com.aemtools.ide.htl

import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.codeInsight.editorActions.wordSelection.AbstractWordSelectioner
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement

/**
 * @author Dmytro Troynikov
 */
class HtlStringLiteralWordSelectioner : AbstractWordSelectioner() {

  override fun canSelect(element: PsiElement): Boolean {
    val _element = element
    return _element is LeafPsiElement
        && _element.elementType in listOf(
        HtlTypes.SINGLE_QUOTED_STRING,
        HtlTypes.DOUBLE_QUOTED_STRING
    )
  }

  override fun select(e: PsiElement?,
                      editorText: CharSequence?,
                      cursorOffset: Int,
                      editor: Editor?): MutableList<TextRange> {
    val ranges = super.select(e, editorText, cursorOffset, editor)

    e?.let {
      val elementRange = e.textRange
      ranges.add(TextRange(elementRange.startOffset + 1, elementRange.endOffset - 1))
    }

    return ranges
  }

}
