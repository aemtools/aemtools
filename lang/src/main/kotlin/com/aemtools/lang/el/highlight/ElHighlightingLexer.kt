package com.aemtools.lang.el.highlight

import com.aemtools.lang.el.lexer.ElLexer
import com.intellij.lexer.LayeredLexer

/**
 * @author Dmytro Troynikov
 */
class ElHighlightingLexer : LayeredLexer(ElLexer())
