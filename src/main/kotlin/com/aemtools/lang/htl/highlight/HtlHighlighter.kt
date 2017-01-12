package com.aemtools.lang.htl.highlight

import com.aemtools.lang.htl.colorscheme.HtlColors
import com.aemtools.lang.htl.lexer.HtlLexer
import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.lexer.LayeredLexer
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

/**
* @author Dmytro_Troynikov
*/
class HtlHighlighter : SyntaxHighlighterBase() {
    override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey> {
        return pack(map(tokenType))
    }

    override fun getHighlightingLexer(): Lexer = HtlHighlightingLexer()

    private fun map(tokenType: IElementType?) = when (tokenType) {
        HtlTypes.TRUE -> HtlColors.BOOLEAN
        HtlTypes.FALSE -> HtlColors.BOOLEAN

        HtlTypes.SINGLE_QUOTED_STRING -> HtlColors.STRING
        HtlTypes.DOUBLE_QUOTED_STRING -> HtlColors.STRING

        HtlTypes.INTEGER -> HtlColors.INTEGER

        HtlTypes.VARIABLE_NAME -> HtlColors.VARIABLE

        else -> null
    }
}

class HtlHighlightingLexer : LayeredLexer(HtlLexer())
