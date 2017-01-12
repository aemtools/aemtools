package com.aemtools.lang.htl.lexer

import com.intellij.lexer.HtmlLexer
import com.intellij.lexer.MergeFunction
import com.intellij.lexer.MergingLexerAdapterBase

/**
 * @author Dmytro Troynikov.
 */
class HtlAttributeLexer : MergingLexerAdapterBase(HtmlLexer()) {
    override fun getMergeFunction(): MergeFunction {
        return MergeFunction { type, originalLexer -> type }
    }
}