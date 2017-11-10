package com.aemtools.lang.el.colorscheme

import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as dc
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r


/**
 * @author Dmytro Troynikov
 */
object ElColors {
  val OPERATOR = r("EL_OPERATOR", dc.OPERATION_SIGN)
  val STRING = r("EL_STRING", dc.STRING)
  val INTEGER = r("EL_INTEGER", dc.NUMBER)
  val IDENTIFIER = r("EL_IDENTIFIER", dc.LOCAL_VARIABLE)

  val PARENTHESES = r("EL_PARENTHESES", dc.PARENTHESES)
  val BRACKET = r("EL_BRACKET", dc.PARENTHESES)

  val DELIMITER = r("EL_DELIMITER", dc.KEYWORD)
}
