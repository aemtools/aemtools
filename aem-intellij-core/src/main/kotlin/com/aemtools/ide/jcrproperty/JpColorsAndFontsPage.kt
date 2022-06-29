package com.aemtools.ide.jcrproperty

import com.aemtools.lang.jcrproperty.colorscheme.JpColors
import com.aemtools.lang.jcrproperty.highlight.JpHighlighter
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class JpColorsAndFontsPage : ColorSettingsPage {

  private val previewTags: MutableMap<String, TextAttributesKey> = mutableMapOf(
      "JP_TYPE" to JpColors.TYPE,
      "JP_DELIMITER" to JpColors.DELIMITER,
      "JP_BRACES" to JpColors.BRACES,
      "JP_BRACKETS" to JpColors.BRACKETS,
      "JP_VALUE" to JpColors.VALUE,
      "JP_VALID_STRING_ESCAPE" to JpColors.VALID_STRING_ESCAPE,
      "JP_INVALID_STRING_ESCAPE" to JpColors.INVALID_STRING_ESCAPE
  )

  private val attributes: Array<AttributesDescriptor> = arrayOf(
      AttributesDescriptor("Type", JpColors.TYPE),
      AttributesDescriptor("Delimiter", JpColors.DELIMITER),
      AttributesDescriptor("Braces", JpColors.BRACES),
      AttributesDescriptor("Value", JpColors.VALUE),
      AttributesDescriptor("Brackets", JpColors.BRACKETS),
      AttributesDescriptor("Valid string escape", JpColors.VALID_STRING_ESCAPE),
      AttributesDescriptor("Invalid string escape", JpColors.INVALID_STRING_ESCAPE)
  )

  override fun getAttributeDescriptors(): Array<AttributesDescriptor> = attributes

  override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

  override fun getDisplayName(): String = "JCR Property Language"

  override fun getIcon(): Icon = AllIcons.FileTypes.Properties

  override fun getHighlighter(): SyntaxHighlighter = JpHighlighter()

  override fun getDemoText(): String = buildString {
    append("<JP_BRACES>{</JP_BRACES><JP_TYPE>String</JP_TYPE><JP_BRACES>}</JP_BRACES>")
    appendLine("<JP_BRACKETS>[</JP_BRACKETS><JP_VALUE>array.value1</JP_VALUE><JP_DELIMITER>,</JP_DELIMITER><JP_VALUE>array.value1</JP_VALUE><JP_BRACKETS>]</JP_BRACKETS>")
    appendLine("<JP_BRACES>{</JP_BRACES><JP_TYPE>Boolean</JP_TYPE><JP_BRACES>}</JP_BRACES><JP_VALUE>true</JP_VALUE>")
    appendLine("<JP_VALUE>simple string value</JP_VALUE>")
    appendLine("""<JP_VALUE>It<JP_VALID_STRING_ESCAPE>\'</JP_VALID_STRING_ESCAPE>s a simple string value</JP_VALUE>""")
    appendLine("""<JP_VALUE>Configure <JP_VALID_STRING_ESCAPE>&amp;</JP_VALID_STRING_ESCAPE> apply color settings</JP_VALUE>""")
    appendLine("""<JP_VALUE>This is invalid - <JP_INVALID_STRING_ESCAPE>\u111</JP_INVALID_STRING_ESCAPE></JP_VALUE>""")
  }

  override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> = previewTags
}
