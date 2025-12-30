package com.aemtools.lang.jcrproperty.lexer

import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet

/**
 * @author Dmytro Primshyts
 */
class JpLexer : MergingLexerAdapter(
    JpRawLexer(),
    TokenSet.create(JpTypes.VALUE_TOKEN, JpTypes.ARRAY_VALUE_TOKEN)
)
