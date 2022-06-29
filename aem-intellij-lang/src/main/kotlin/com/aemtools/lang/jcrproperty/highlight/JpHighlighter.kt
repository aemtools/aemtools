package com.aemtools.lang.jcrproperty.highlight

import com.aemtools.lang.jcrproperty.colorscheme.JpColors
import com.aemtools.lang.jcrproperty.psi.JpTypes.*
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class JpHighlighter : SyntaxHighlighterBase() {
  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
    return pack(map(tokenType))
  }

  override fun getHighlightingLexer(): Lexer = JpHighlightingLexer()

  private fun map(tokenType: IElementType?) = when (tokenType) {
    LBRACE,
    RBRACE -> JpColors.BRACES

    LBRACKET,
    RBRACKET -> JpColors.BRACKETS

    COMMA -> JpColors.DELIMITER

    VALUE_TOKEN, ARRAY_VALUE_TOKEN -> JpColors.VALUE

    VALID_STRING_ESCAPE_TOKEN,
    VALID_XML_ENTITY_REF_ESCAPE_TOKEN,
    VALID_XML_CHAR_REF_ESCAPE_TOKEN -> JpColors.VALID_STRING_ESCAPE

    INVALID_CHARACTER_ESCAPE_TOKEN,
    INVALID_UNICODE_ESCAPE_TOKEN -> JpColors.INVALID_STRING_ESCAPE

    BINARY, BOOLEAN,
    DATE, DECIMAL,
    DOUBLE, LONG,
    NAME, PATH,
    STRING, URI, REFERENCE,
    WEAK_REFERENCE -> JpColors.TYPE
    else -> null
  }

}
