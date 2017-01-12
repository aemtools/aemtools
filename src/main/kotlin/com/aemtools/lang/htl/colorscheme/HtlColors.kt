package com.aemtools.lang.htl.colorscheme


import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r

/**
 * @author Dmytro_Troynikov
 */
object HtlColors {
    val BOOLEAN = r("htl.el.BOOLEAN", Default.KEYWORD)
    val STRING = r("htl.sel.STRING", Default.STRING)
    val INTEGER = r("htl.sel.INTEGER", Default.NUMBER)
    val VARIABLE = r("htl.sel.VARIABLE", Default.LOCAL_VARIABLE)
}