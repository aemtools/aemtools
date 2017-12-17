package com.aemtools.lang.clientlib.highlight

import com.aemtools.lang.clientlib.lexer.CdLexer
import com.aemtools.lang.clientlib.psi.CdTypes
import com.intellij.lexer.LayeredLexer
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro_Troynikov
 */
class CdHighlighter : SyntaxHighlighterBase() {
  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
    return pack(map(tokenType))
  }

  private fun map(tokenType: IElementType?) = when (tokenType) {
    CdTypes.COMMENT_TOKEN -> CdColors.COMMENT
    CdTypes.PREFIX_TOKEN -> CdColors.PREFIX
    CdTypes.DD, CdTypes.DOT -> CdColors.DOT
    CdTypes.SEPARATOR,
    CdTypes.WORD -> CdColors.IMPORT
    else -> null
  }

  override fun getHighlightingLexer(): Lexer = LayeredLexer(CdLexer())

}
