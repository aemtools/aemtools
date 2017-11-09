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
}
