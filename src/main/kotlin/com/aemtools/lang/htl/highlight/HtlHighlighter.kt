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
    HtlTypes.FALSE -> HtlColors.BOOLEAN

    HtlTypes.SINGLE_QUOTED_STRING,
    HtlTypes.DOUBLE_QUOTED_STRING -> HtlColors.STRING

    HtlTypes.INTEGER -> HtlColors.INTEGER

    HtlTypes.NULL_LITERAL_TOKEN -> HtlColors.NULL

    HtlTypes.VARIABLE_NAME -> HtlColors.VARIABLE

    HtlTypes.AT -> HtlColors.DELIMITER

    HtlTypes.LBRACE,
    HtlTypes.EL_START,
    HtlTypes.RBRACE -> HtlColors.DELIMITER

    HtlTypes.LBRACKET,
    HtlTypes.RBRACKET -> HtlColors.PARENTHESES

    HtlTypes.GT,
    HtlTypes.LT,
    HtlTypes.GTE,
    HtlTypes.LTE,
    HtlTypes.AND_AND,
    HtlTypes.OR_OR -> HtlColors.OPERATOR

    HtlTypes.VAR_NAME -> HtlColors.IDENTIFIER

    HtlTypes.LSQRBRACKET,
    HtlTypes.RSQRBRACKET -> HtlColors.BRACKET

    else -> null
  }
}
