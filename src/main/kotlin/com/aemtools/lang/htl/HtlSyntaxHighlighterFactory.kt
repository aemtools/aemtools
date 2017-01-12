package com.aemtools.lang.htl

import com.intellij.lexer.HtmlHighlightingLexer
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

/**
 * @author Dmytro_Troynikov
 */
class HtlSyntaxHighlighterFactory : SingleLazyInstanceSyntaxHighlighterFactory() {

    override fun createHighlighter(): SyntaxHighlighter {
        throw UnsupportedOperationException()
    }

    private object HtlSyntaxHighlighter : HtmlHighlightingLexer() {

    }

}