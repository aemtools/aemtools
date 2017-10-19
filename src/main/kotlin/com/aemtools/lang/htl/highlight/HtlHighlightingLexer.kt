package com.aemtools.lang.htl.highlight

import com.aemtools.lang.htl.lexer.HtlLexer
import com.intellij.lexer.LayeredLexer

/**
 * @author Dmytro Troynikov
 */
class HtlHighlightingLexer : LayeredLexer(HtlLexer())
