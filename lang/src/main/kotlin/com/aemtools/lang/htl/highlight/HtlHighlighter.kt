package com.aemtools.lang.htl.highlight

import com.aemtools.lang.htl.colorscheme.HtlColors
import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Troynikov
 */
class HtlHighlighter : SyntaxHighlighterBase() {
  override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey> {
    return pack(map(tokenType))
  }

  override fun getHighlightingLexer(): Lexer = HtlHighlightingLexer()

  private fun map(tokenType: IElementType?) = when (tokenType) {
    HtlTypes.TRUE,
    HtlTypes.FALSE -> com.aemtools.lang.htl.colorscheme.HtlColors.BOOLEAN

    HtlTypes.SINGLE_QUOTED_STRING,
    HtlTypes.DOUBLE_QUOTED_STRING -> com.aemtools.lang.htl.colorscheme.HtlColors.STRING

    HtlTypes.INTEGER -> com.aemtools.lang.htl.colorscheme.HtlColors.INTEGER

    HtlTypes.NULL_LITERAL_TOKEN -> com.aemtools.lang.htl.colorscheme.HtlColors.NULL

    HtlTypes.VARIABLE_NAME -> com.aemtools.lang.htl.colorscheme.HtlColors.VARIABLE

    HtlTypes.AT -> com.aemtools.lang.htl.colorscheme.HtlColors.DELIMITER

    HtlTypes.LBRACE,
    HtlTypes.EL_START,
    HtlTypes.RBRACE -> com.aemtools.lang.htl.colorscheme.HtlColors.DELIMITER

    HtlTypes.LBRACKET,
    HtlTypes.RBRACKET -> com.aemtools.lang.htl.colorscheme.HtlColors.PARENTHESES

    HtlTypes.GT,
    HtlTypes.LT,
    HtlTypes.GTE,
    HtlTypes.LTE,
    HtlTypes.AND_AND,
    HtlTypes.OR_OR -> com.aemtools.lang.htl.colorscheme.HtlColors.OPERATOR

    HtlTypes.VAR_NAME -> com.aemtools.lang.htl.colorscheme.HtlColors.IDENTIFIER

    HtlTypes.LSQRBRACKET,
    HtlTypes.RSQRBRACKET -> com.aemtools.lang.htl.colorscheme.HtlColors.BRACKET

    else -> null
  }
}
