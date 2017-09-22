package com.aemtools.reference.htl

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.lang.Language
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.xml.XmlAttribute
import javax.swing.Icon

/**
 * Htl declaration identifier fake element.
 *
 * @author Dmytro Troynikov
 */
open class HtlDeclarationIdentifier(val xmlAttribute: XmlAttribute)
  : FakePsiElement(), PsiNamedElement {

  override fun getText(): String? {
    return xmlAttribute.name.substringAfter(".", "item")
  }

  override fun getParent(): PsiElement {
    return xmlAttribute
  }

  override fun getName(): String? {
    val variableName = if (xmlAttribute.name.contains(".")) {
      xmlAttribute.name.substringAfter(".")
    } else {
      "item"
    }

    return variableName
  }

  override fun setName(name: String): PsiElement {
    val attributeName = if (xmlAttribute.name.contains(".")) {
      xmlAttribute.name.substringBefore(".")
    } else {
      xmlAttribute.name
    }

    return xmlAttribute.setName("$attributeName.$name")
  }

  override fun toString(): String {
    return xmlAttribute.text
  }

  override fun getTextRange(): TextRange? {
    return if (xmlAttribute.name.contains(".")) {
      val start = xmlAttribute.textRange.startOffset + xmlAttribute.name.indexOf(".") + 1
      TextRange(start, start + (name?.length ?: xmlAttribute.nameElement.textLength))
    } else {
      xmlAttribute.nameElement.textRange
    }
  }

  override fun getTextOffset(): Int {
    return if (xmlAttribute.name.contains(".")) {
      xmlAttribute.nameElement.textOffset + xmlAttribute.name.indexOf(".") + 1
    } else {
      xmlAttribute.nameElement.textOffset
    }
  }

  override fun getStartOffsetInParent(): Int {
    return if (xmlAttribute.name.contains(".")) {
      xmlAttribute.startOffsetInParent + xmlAttribute.name.indexOf(".") + 1
    } else {
      xmlAttribute.startOffsetInParent
    }
  }

  override fun getTextLength(): Int {
    return name?.length ?: 0
  }

  override fun textToCharArray(): CharArray {
    return text?.toCharArray() ?: kotlin.CharArray(0)
  }

  override fun textContains(c: Char): Boolean {
    return textToCharArray().contains(c)
  }

  override fun getPresentableText(): String? {
    return "HTL Variable"
  }

  override fun getLocationString(): String? {
    return xmlAttribute.containingFile.name
  }

  override fun getIcon(open: Boolean): Icon? {
    return HtlIcons.SLY_USE_VARIABLE_ICON
  }

  override fun isPhysical(): Boolean = false

  override fun getLanguage(): Language = HtlLanguage

  override fun getContainingFile(): PsiFile {
    return xmlAttribute.containingFile
  }

  override fun equals(other: Any?): Boolean {
    return when (other) {
      is HtlDeclarationIdentifier -> other.xmlAttribute == xmlAttribute
      is HtlListHelperDeclarationIdentifier -> other.attribute == xmlAttribute
      is XmlAttribute -> xmlAttribute == other
      else -> false
    }
  }

  override fun hashCode(): Int {
    return toString().hashCode()
  }
}
