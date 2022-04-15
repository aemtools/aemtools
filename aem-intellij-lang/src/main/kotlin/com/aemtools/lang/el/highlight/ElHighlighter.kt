package com.aemtools.lang.el.highlight

import com.aemtools.lang.el.colorscheme.ElColors
import com.aemtools.lang.el.psi.ElTypes.AND0
import com.aemtools.lang.el.psi.ElTypes.AND1
import com.aemtools.lang.el.psi.ElTypes.BOOLEAN_LITERAL
import com.aemtools.lang.el.psi.ElTypes.EMPTY
import com.aemtools.lang.el.psi.ElTypes.END_EXPRESSION
import com.aemtools.lang.el.psi.ElTypes.GE0
import com.aemtools.lang.el.psi.ElTypes.GE1
import com.aemtools.lang.el.psi.ElTypes.GT0
import com.aemtools.lang.el.psi.ElTypes.IDENTIFIER_TOKEN
import com.aemtools.lang.el.psi.ElTypes.INTEGER_LITERAL
import com.aemtools.lang.el.psi.ElTypes.LBRACK
import com.aemtools.lang.el.psi.ElTypes.LE0
import com.aemtools.lang.el.psi.ElTypes.LE1
import com.aemtools.lang.el.psi.ElTypes.LPAREN
import com.aemtools.lang.el.psi.ElTypes.LT0
import com.aemtools.lang.el.psi.ElTypes.LT1
import com.aemtools.lang.el.psi.ElTypes.NULL_LITERAL_TOKEN
import com.aemtools.lang.el.psi.ElTypes.OR0
import com.aemtools.lang.el.psi.ElTypes.OR1
import com.aemtools.lang.el.psi.ElTypes.RBRACK
import com.aemtools.lang.el.psi.ElTypes.RPAREN
import com.aemtools.lang.el.psi.ElTypes.START_DEFERRED_EXPRESSION
import com.aemtools.lang.el.psi.ElTypes.START_DYNAMIC_EXPRESSION
import com.aemtools.lang.el.psi.ElTypes.STRING_LITERAL
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class ElHighlighter : SyntaxHighlighterBase() {
  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
    return pack(map(tokenType))
  }

  override fun getHighlightingLexer(): Lexer = ElHighlightingLexer()

  private fun map(tokenType: IElementType?) = when (tokenType) {
    AND0, AND1,
    OR0, OR1,
    LE0, LE1,
    GE0, GE1,
    LT0, LT1,
    GT0, GT0,
    NULL_LITERAL_TOKEN,
    EMPTY,
    BOOLEAN_LITERAL -> ElColors.OPERATOR

    STRING_LITERAL -> ElColors.STRING

    INTEGER_LITERAL -> ElColors.INTEGER

    IDENTIFIER_TOKEN -> ElColors.IDENTIFIER

    LBRACK, RBRACK -> ElColors.BRACKET

    LPAREN, RPAREN -> ElColors.PARENTHESES

    START_DEFERRED_EXPRESSION,
    START_DYNAMIC_EXPRESSION,
    END_EXPRESSION -> ElColors.DELIMITER

    else -> null
  }

}
