package com.aemtools.lang.jcrproperty.highlight

import com.aemtools.lang.jcrproperty.lexer.JpLexer
import com.intellij.lexer.LayeredLexer

/**
 * @author Dmytro Primshyts
 */
class JpHighlightingLexer : LayeredLexer(JpLexer())
