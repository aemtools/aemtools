package com.aemtools.lang.htl.colorscheme

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as dc
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r

/**
 * @author Dmytro_Troynikov
 */
object HtlColors {
    val BOOLEAN = r("htl.el.BOOLEAN", dc.KEYWORD)
    val STRING = r("htl.sel.STRING", dc.STRING)
    val INTEGER = r("htl.sel.INTEGER", dc.NUMBER)
    val VARIABLE = r("htl.sel.VARIABLE", dc.LOCAL_VARIABLE)
    val DELIMITER = r("htl.sel.AT", dc.KEYWORD)
    val BRACE = r("htl.sel.BRACE", dc.BRACES)
    val BRACKET = r("htl.sel.BRACKET", dc.BRACKETS)
    val OPERATOR = r("htl.sel.OPERATOR", dc.OPERATION_SIGN)
    val IDENTIFIER = r("htl.sel.IDENTIFIER", dc.IDENTIFIER)
    val TEMPLATE_ARGUMENT = r("htl.sel.TEMPLATE_ARGUMENT", dc.PARAMETER)
    val STANDARD_OPTION = r("htl.sel.STANDARD_OPTION", dc.PARAMETER)
    val NULL = r("htl.sel.NULL", dc.KEYWORD)
    val PARENTHESES = r("htl.sel.PARENTHESES", dc.PARENTHESES)
}
