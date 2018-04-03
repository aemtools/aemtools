package com.aemtools.lang.jcrproperty.highlight

import com.aemtools.lang.jcrproperty.colorscheme.JpColors
import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.aemtools.lang.jcrproperty.psi.JpTypes.BINARY
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
    JpTypes.LBRACE,
    JpTypes.RBRACE -> JpColors.DELIMITER

    JpTypes.LBRACE,
    JpTypes.RBRACE -> JpColors.BRACES

    BINARY, JpTypes.BOOLEAN,
    JpTypes.DATE, JpTypes.DECIMAL,
    JpTypes.DOUBLE, JpTypes.LONG,
    JpTypes.NAME, JpTypes.PATH,
    JpTypes.STRING, JpTypes.URI, JpTypes.REFERENCE,
    JpTypes.WEAK_REFERENCE -> JpColors.TYPE
    else -> null
  }

}
