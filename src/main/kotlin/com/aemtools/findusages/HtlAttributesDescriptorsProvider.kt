package com.aemtools.findusages

import com.aemtools.util.isHtlAttributeName
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlAttributeDeclImpl
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider
import com.intellij.xml.impl.BasicXmlAttributeDescriptor
import com.intellij.xml.impl.XmlAttributeDescriptorEx

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

class HtlAttributeNameDeclaration(val attribute: XmlAttribute) : XmlAttributeDeclImpl() {

}