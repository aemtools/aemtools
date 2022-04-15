package com.aemtools.lang.htl.lexer

import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet

/**
 * @author Dmytro Primshyts
 */
class HtlLexer : MergingLexerAdapter(HtlRawLexer(),
    TokenSet.create(
        HtlTypes.OUTER_LANGUAGE,
        HtlTypes.COMMENT_TOKEN,
        HtlTypes.STRING_CONTENT
    )
)
