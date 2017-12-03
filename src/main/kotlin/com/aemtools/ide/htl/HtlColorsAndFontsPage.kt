package com.aemtools.ide.htl

import com.aemtools.common.constant.const.DOLLAR
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
      "HTL_EL_BOOLEAN" to com.aemtools.lang.htl.colorscheme.HtlColors.BOOLEAN,
      "HTL_EL_STRING" to com.aemtools.lang.htl.colorscheme.HtlColors.STRING,
      "HTL_EL_INTEGER" to com.aemtools.lang.htl.colorscheme.HtlColors.INTEGER,
      "HTL_El_VARIABLE" to com.aemtools.lang.htl.colorscheme.HtlColors.VARIABLE,
      "HTL_El_AT" to com.aemtools.lang.htl.colorscheme.HtlColors.DELIMITER,
      "HTL_EL_BRACKET" to com.aemtools.lang.htl.colorscheme.HtlColors.BRACKET,
      "HTL_EL_OPERATOR" to com.aemtools.lang.htl.colorscheme.HtlColors.OPERATOR,
      "HTL_EL_IDENTIFIER" to com.aemtools.lang.htl.colorscheme.HtlColors.IDENTIFIER,
      "HTL_EL_TEMPLATE_ARGUMENT" to com.aemtools.lang.htl.colorscheme.HtlColors.TEMPLATE_ARGUMENT,
      "HTL_EL_TEMPLATE_PARAMETER" to com.aemtools.lang.htl.colorscheme.HtlColors.TEMPLATE_PARAMETER,
      "HTL_EL_STANDARD_OPTION" to com.aemtools.lang.htl.colorscheme.HtlColors.STANDARD_OPTION,
      "HTL_EL_OPTION" to com.aemtools.lang.htl.colorscheme.HtlColors.OPTION,
      "HTL_EL_NULL" to com.aemtools.lang.htl.colorscheme.HtlColors.NULL,
      "HTL_EL_PARENTHESES" to com.aemtools.lang.htl.colorscheme.HtlColors.PARENTHESES,

      "HTL_ATTRIBUTE" to com.aemtools.lang.htl.colorscheme.HtlColors.HTL_ATTRIBUTE,

      "HTL_VARIABLE_DECLARATION" to com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_DECLARATION,
      "HTL_VARIABLE_UNUSED" to com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_UNUSED,

      "HTL_EL_GLOBAL_VARIABLE" to com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_GLOBAL_VARIABLE,
      "HTL_EL_LOCAL_VARIABLE" to com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_LOCAL_VARIABLE,
      "HTL_EL_UNRESOLVED_VARIABLE" to com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_UNRESOLVED_VARIABLE
  )

  private val attributes: Array<AttributesDescriptor> = arrayOf(
      AttributesDescriptor("Boolean", com.aemtools.lang.htl.colorscheme.HtlColors.BOOLEAN),
      AttributesDescriptor("String", com.aemtools.lang.htl.colorscheme.HtlColors.STRING),
      AttributesDescriptor("Integer", com.aemtools.lang.htl.colorscheme.HtlColors.INTEGER),
      AttributesDescriptor("Variable", com.aemtools.lang.htl.colorscheme.HtlColors.VARIABLE),
      AttributesDescriptor("Delimiter", com.aemtools.lang.htl.colorscheme.HtlColors.DELIMITER),
      AttributesDescriptor("Bracket", com.aemtools.lang.htl.colorscheme.HtlColors.BRACKET),
      AttributesDescriptor("Operator", com.aemtools.lang.htl.colorscheme.HtlColors.OPERATOR),
      AttributesDescriptor("Identifier", com.aemtools.lang.htl.colorscheme.HtlColors.IDENTIFIER),
      AttributesDescriptor("Template Argument", com.aemtools.lang.htl.colorscheme.HtlColors.TEMPLATE_ARGUMENT),
      AttributesDescriptor("Template Parameter", com.aemtools.lang.htl.colorscheme.HtlColors.TEMPLATE_PARAMETER),
      AttributesDescriptor("Standard Option", com.aemtools.lang.htl.colorscheme.HtlColors.STANDARD_OPTION),
      AttributesDescriptor("Option", com.aemtools.lang.htl.colorscheme.HtlColors.OPTION),
      AttributesDescriptor("Null", com.aemtools.lang.htl.colorscheme.HtlColors.NULL),
      AttributesDescriptor("Parentheses", com.aemtools.lang.htl.colorscheme.HtlColors.PARENTHESES),

      AttributesDescriptor("HTL Attribute", com.aemtools.lang.htl.colorscheme.HtlColors.HTL_ATTRIBUTE),

      AttributesDescriptor("Variable Declaration", com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_DECLARATION),
      AttributesDescriptor("Unused Variable", com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_UNUSED),

      AttributesDescriptor("Global Variable", com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_GLOBAL_VARIABLE),
      AttributesDescriptor("Local Variable", com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_LOCAL_VARIABLE),
      AttributesDescriptor("Unresolved Variable", com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_UNRESOLVED_VARIABLE)
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
