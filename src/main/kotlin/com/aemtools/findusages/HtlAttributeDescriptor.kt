package com.aemtools.findusages

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlAttributeDeclImpl
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.impl.BasicXmlAttributeDescriptor
import com.intellij.xml.impl.XmlAttributeDescriptorEx

/**
 * @author Dmytro Troynikov
 */
class HtlAttributeDescriptor(val attributeName: String, val parentTag: XmlTag)
  : BasicXmlAttributeDescriptor(), XmlAttributeDescriptorEx {
  override fun getDefaultValue(): String? {
    return ""
  }

  override fun validateValue(context: XmlElement?, value: String?): String? = null

  override fun getDependences(): Array<Any> = emptyArray()

  override fun getName(context: PsiElement?): String {
    return attributeName
  }

  override fun getName(): String {
    return attributeName
  }

  override fun isRequired(): Boolean = false

  override fun hasIdRefType(): Boolean {
    return false
  }

  override fun init(element: PsiElement?) {
  }

  override fun isFixed(): Boolean {
    return false
  }

  override fun getDeclaration(): PsiElement {
    return parentTag.getAttribute(attributeName) ?: XmlAttributeDeclImpl()
  }

  override fun isEnumerated(): Boolean = false

  override fun getEnumeratedValues(): Array<String>? = null

  override fun hasIdType(): Boolean = false

  override fun handleTargetRename(newTargetName: String): String? {
    parentTag.getAttribute(attributeName)?.name = newTargetName
    return newTargetName
  }

}
