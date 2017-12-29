package com.aemtools.findusages

import com.aemtools.common.util.isHtlAttributeName
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider

/**
 * @author Dmytro Troynikov
 */
class HtlAttributesDescriptorsProvider : XmlAttributeDescriptorsProvider {
  override fun getAttributeDescriptors(context: XmlTag?): Array<XmlAttributeDescriptor> {
    return arrayOf()
  }

  override fun getAttributeDescriptor(attributeName: String?, context: XmlTag?): XmlAttributeDescriptor? {
    if (attributeName != null
        && context != null
        && attributeName.isHtlAttributeName()) {
      return HtlAttributeDescriptor(attributeName, context)
    }

    return null
  }

}
