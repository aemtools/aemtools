package com.aemtools.lang.el.highlight

import com.aemtools.lang.el.colorscheme.ElColors
import com.aemtools.lang.el.psi.ElTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Troynikov
 */
class ElHighlighter : SyntaxHighlighterBase() {
  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
    return pack(map(tokenType))
  }

  override fun getHighlightingLexer(): Lexer = ElHighlightingLexer()

  private fun map(tokenType: IElementType?) = when (tokenType) {
    ElTypes.AND0,
    ElTypes.AND1,
    ElTypes.OR0,
    ElTypes.OR1 -> ElColors.OPERATOR

    else -> null
  }

}
