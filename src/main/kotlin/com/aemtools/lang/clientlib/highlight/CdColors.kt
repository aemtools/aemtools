package com.aemtools.lang.clientlib.highlight

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as dc
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r

/**
 * @author Dmytro Troynikov
 */
object CdColors {
  val COMMENT = r("cd.COMMENT", dc.LINE_COMMENT)
  val IMPORT = r("cd.IMPORT", dc.IDENTIFIER)
  val DOT = r("cd.DOT", dc.DOT)
  val PREFIX = r("cd.PREFIX", dc.CONSTANT)
}
