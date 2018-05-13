package com.aemtools.lang.jcrproperty.highlight

import com.aemtools.lang.jcrproperty.colorscheme.JpColors
import com.aemtools.lang.jcrproperty.psi.JpTypes.BINARY
import com.aemtools.lang.jcrproperty.psi.JpTypes.BOOLEAN
import com.aemtools.lang.jcrproperty.psi.JpTypes.COMMA
import com.aemtools.lang.jcrproperty.psi.JpTypes.DATE
import com.aemtools.lang.jcrproperty.psi.JpTypes.DECIMAL
import com.aemtools.lang.jcrproperty.psi.JpTypes.DOUBLE
import com.aemtools.lang.jcrproperty.psi.JpTypes.LBRACE
import com.aemtools.lang.jcrproperty.psi.JpTypes.LBRACKET
import com.aemtools.lang.jcrproperty.psi.JpTypes.LONG
import com.aemtools.lang.jcrproperty.psi.JpTypes.NAME
import com.aemtools.lang.jcrproperty.psi.JpTypes.PATH
import com.aemtools.lang.jcrproperty.psi.JpTypes.RBRACE
import com.aemtools.lang.jcrproperty.psi.JpTypes.RBRACKET
import com.aemtools.lang.jcrproperty.psi.JpTypes.REFERENCE
import com.aemtools.lang.jcrproperty.psi.JpTypes.STRING
import com.aemtools.lang.jcrproperty.psi.JpTypes.TYPE
import com.aemtools.lang.jcrproperty.psi.JpTypes.URI
import com.aemtools.lang.jcrproperty.psi.JpTypes.WEAK_REFERENCE
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
    RBRACE -> JpColors.DELIMITER

    LBRACKET,
    RBRACKET -> JpColors.BRACES

    COMMA -> JpColors.TYPE

    BINARY, BOOLEAN,
    DATE, DECIMAL,
    DOUBLE, LONG,
    NAME, PATH,
    STRING, URI, REFERENCE,
    WEAK_REFERENCE -> JpColors.TYPE
    else -> null
  }

}
