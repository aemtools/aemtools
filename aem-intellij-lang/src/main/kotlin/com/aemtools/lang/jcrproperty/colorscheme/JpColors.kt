package com.aemtools.lang.jcrproperty.colorscheme

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as dc
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r

/**
 * @author Dmytro Primshyts
 */
object JpColors {
  val TYPE = r("JP_TYPE", dc.KEYWORD)
  val DELIMITER = r("JP_DELIMITER", dc.KEYWORD)
  val BRACES = r("JP_BRACES", dc.KEYWORD)
}
