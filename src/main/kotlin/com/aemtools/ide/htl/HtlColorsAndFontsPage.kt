package com.aemtools.ide.htl

import com.aemtools.constant.const.DOLLAR
import com.aemtools.lang.htl.colorscheme.HtlColors
import com.aemtools.lang.htl.highlight.HtlHighlighter
import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
class HtlColorsAndFontsPage : ColorSettingsPage {

  private val previewTags: MutableMap<String, TextAttributesKey> = mutableMapOf(
      "HTL_EL_BOOLEAN" to HtlColors.BOOLEAN,
      "HTL_EL_STRING" to HtlColors.STRING,
      "HTL_EL_INTEGER" to HtlColors.INTEGER,
      "HTL_El_VARIABLE" to HtlColors.VARIABLE,
      "HTL_El_AT" to HtlColors.DELIMITER,
      "HTL_EL_BRACKET" to HtlColors.BRACKET,
      "HTL_EL_OPERATOR" to HtlColors.OPERATOR,
      "HTL_EL_IDENTIFIER" to HtlColors.IDENTIFIER,
      "HTL_EL_TEMPLATE_ARGUMENT" to HtlColors.TEMPLATE_ARGUMENT,
      "HTL_EL_TEMPLATE_PARAMETER" to HtlColors.TEMPLATE_PARAMETER,
      "HTL_EL_STANDARD_OPTION" to HtlColors.STANDARD_OPTION,
      "HTL_EL_OPTION" to HtlColors.OPTION,
      "HTL_EL_NULL" to HtlColors.NULL,
      "HTL_EL_PARENTHESES" to HtlColors.PARENTHESES,

      "HTL_ATTRIBUTE" to HtlColors.HTL_ATTRIBUTE,

      "HTL_VARIABLE_DECLARATION" to HtlColors.HTL_VARIABLE_DECLARATION,
      "HTL_VARIABLE_UNUSED" to HtlColors.HTL_VARIABLE_UNUSED,

      "HTL_EL_GLOBAL_VARIABLE" to HtlColors.HTL_EL_GLOBAL_VARIABLE,
      "HTL_EL_LOCAL_VARIABLE" to HtlColors.HTL_EL_LOCAL_VARIABLE,
      "HTL_EL_UNRESOLVED_VARIABLE" to HtlColors.HTL_EL_UNRESOLVED_VARIABLE
  )

  private val attributes: Array<AttributesDescriptor> = arrayOf(
      AttributesDescriptor("Boolean", HtlColors.BOOLEAN),
      AttributesDescriptor("String", HtlColors.STRING),
      AttributesDescriptor("Integer", HtlColors.INTEGER),
      AttributesDescriptor("Variable", HtlColors.VARIABLE),
      AttributesDescriptor("Delimiter", HtlColors.DELIMITER),
      AttributesDescriptor("Bracket", HtlColors.BRACKET),
      AttributesDescriptor("Operator", HtlColors.OPERATOR),
      AttributesDescriptor("Identifier", HtlColors.IDENTIFIER),
      AttributesDescriptor("Template Argument", HtlColors.TEMPLATE_ARGUMENT),
      AttributesDescriptor("Template Parameter", HtlColors.TEMPLATE_PARAMETER),
      AttributesDescriptor("Standard Option", HtlColors.STANDARD_OPTION),
      AttributesDescriptor("Option", HtlColors.OPTION),
      AttributesDescriptor("Null", HtlColors.NULL),
      AttributesDescriptor("Parentheses", HtlColors.PARENTHESES),

      AttributesDescriptor("HTL Attribute", HtlColors.HTL_ATTRIBUTE),

      AttributesDescriptor("Variable Declaration", HtlColors.HTL_VARIABLE_DECLARATION),
      AttributesDescriptor("Unused Variable", HtlColors.HTL_VARIABLE_UNUSED),

      AttributesDescriptor("Global Variable", HtlColors.HTL_EL_GLOBAL_VARIABLE),
      AttributesDescriptor("Local Variable", HtlColors.HTL_EL_LOCAL_VARIABLE),
      AttributesDescriptor("Unresolved Variable", HtlColors.HTL_EL_UNRESOLVED_VARIABLE)
  )

  override fun getHighlighter(): SyntaxHighlighter
      = HtlHighlighter()

  override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>
      = previewTags

  override fun getIcon(): Icon?
      = HtlIcons.HTL_FILE_ICON

  override fun getAttributeDescriptors(): Array<AttributesDescriptor>
      = attributes

  override fun getColorDescriptors(): Array<ColorDescriptor>
      = ColorDescriptor.EMPTY_ARRAY

  override fun getDisplayName(): String
      = "HTML Markup Language (HTL)"

  override fun getDemoText(): String = buildString {
    append("<div")
    append(" <HTL_ATTRIBUTE>data-sly-use.</HTL_ATTRIBUTE><HTL_VARIABLE_DECLARATION>bean</HTL_VARIABLE_DECLARATION>")
    append("$DOLLAR{'com.test.Bean' @ <HTL_EL_OPTION>option</HTL_EL_OPTION>=true}\"")
    append(">\n")
    append("$DOLLAR{<HTL_EL_LOCAL_VARIABLE>bean</HTL_EL_LOCAL_VARIABLE>.field @ ")
    append("<HTL_EL_STANDARD_OPTION>context</HTL_EL_STANDARD_OPTION>='html'}\n")
    append("$DOLLAR{true || false || null || (100 < 200)}\n")
    append("$DOLLAR{<HTL_EL_GLOBAL_VARIABLE>properties</HTL_EL_GLOBAL_VARIABLE>[jcr:title]}\n")
    append("$DOLLAR{<HTL_EL_UNRESOLVED_VARIABLE>unresolved</HTL_EL_UNRESOLVED_VARIABLE>}\n")
    append("<div <HTL_ATTRIBUTE>data-sly-test.</HTL_ATTRIBUTE>")
    append("<HTL_VARIABLE_UNUSED>unused</HTL_VARIABLE_UNUSED>=\"\">\n")
    append("<div <HTL_ATTRIBUTE>data-sly-template.</HTL_ATTRIBUTE>")
    append("<HTL_VARIABLE_DECLARATION>template</HTL_VARIABLE_DECLARATION>=\"")
    append("$DOLLAR{@ <HTL_EL_TEMPLATE_PARAMETER>param1</HTL_EL_TEMPLATE_PARAMETER>}\"></div>\n")

    append("<div <HTL_ATTRIBUTE>data-sly-call</HTL_ATTRIBUTE>=\"")
    append("$DOLLAR{template @ <HTL_EL_TEMPLATE_ARGUMENT>param1</HTL_EL_TEMPLATE_ARGUMENT>=true}\"\n")
  }

}
