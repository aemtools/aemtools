package com.aemtools.lang.common

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.editor.colors.TextAttributesKey

/**
 * @author Dmytro Troynikov
 */
object HtlHighlightColors {
    val HTL_VARIABLE_DECLARATION = key("HTL_VARIABLE_DECLARATION", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
    val HTL_ATTRIBUTE = key("HTL_ATTRIBUTE", XmlHighlighterColors.HTML_ATTRIBUTE_NAME)
    val HTL_VARIABLE_UNUSED = key("HTL_VARIABLE_UNUSED", CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES)

    val HTL_EL_GLOBAL_VARIABLE = key("HTL_EL_GLOBAL_VARIABLE", DefaultLanguageHighlighterColors.CONSTANT)
    val HTL_EL_LOCAL_VARIABLE = key("HTL_EL_LOCAL_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
    val HTL_EL_UNRESOLVED_VARIABLE = key("HTL_EL_UNRESOLVED_VARIABLE", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES)

    private fun key(name: String, key:TextAttributesKey) =
            TextAttributesKey.createTextAttributesKey(name, key)
}