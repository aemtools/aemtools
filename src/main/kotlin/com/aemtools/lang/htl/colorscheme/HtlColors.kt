package com.aemtools.lang.htl.colorscheme

import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as dc
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r

/**
 * @author Dmytro_Troynikov
 */
object HtlColors {
  val BOOLEAN = r("HTL_EL_BOOLEAN", dc.KEYWORD)
  val STRING = r("HTL_EL_STRING", dc.STRING)
  val INTEGER = r("HTL_EL_INTEGER", dc.NUMBER)
  val VARIABLE = r("HTL_EL_VARIABLE", dc.LOCAL_VARIABLE)
  val DELIMITER = r("HTL_EL_AT", dc.KEYWORD)
  val BRACKET = r("HTL_EL_BRACKET", dc.BRACKETS)
  val OPERATOR = r("HTL_EL_OPERATOR", dc.OPERATION_SIGN)
  val IDENTIFIER = r("HTL_EL_IDENTIFIER", dc.IDENTIFIER)

  val TEMPLATE_ARGUMENT = r("HTL_EL_TEMPLATE_ARGUMENT", dc.PARAMETER)
  val TEMPLATE_PARAMETER = r("HTL_EL_TEMPLATE_PARAMETER", dc.PARAMETER)
  val STANDARD_OPTION = r("HTL_EL_STANDARD_OPTION", dc.PARAMETER)
  val OPTION = r("HTL_EL_OPTION", dc.PARAMETER)

  val NULL = r("HTL_EL_NULL", dc.KEYWORD)
  val PARENTHESES = r("HTL_EL_PARENTHESES", dc.PARENTHESES)

  val HTL_ATTRIBUTE = r("HTL_ATTRIBUTE", XmlHighlighterColors.HTML_ATTRIBUTE_NAME)

  val HTL_VARIABLE_DECLARATION = r("HTL_VARIABLE_DECLARATION", dc.INSTANCE_FIELD)
  val HTL_VARIABLE_UNUSED = r("HTL_VARIABLE_UNUSED", CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES)

  val HTL_EL_GLOBAL_VARIABLE = r("HTL_EL_GLOBAL_VARIABLE", dc.CONSTANT)
  val HTL_EL_LOCAL_VARIABLE = r("HTL_EL_LOCAL_VARIABLE", dc.LOCAL_VARIABLE)
  val HTL_EL_UNRESOLVED_VARIABLE = r("HTL_EL_UNRESOLVED_VARIABLE", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES)

}
