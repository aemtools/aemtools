package com.aemtools.lang.html

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.isHtlAttribute
import com.intellij.psi.impl.source.html.dtd.HtmlAttributeDescriptorImpl
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider
import com.intellij.xml.impl.BasicXmlAttributeDescriptor

/**
 * @author Dmytro Troynikov
 */
class  HtlAttributeDescriptorsProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(tag: XmlTag?): Array<out XmlAttributeDescriptor> {
        if (tag == null) {
            return arrayOf()
        }

        val attributes = tag.findChildrenByType(XmlAttribute::class.java)

//        val result = attributes.filter { it.isHtlAttribute() }
//                .map ( object : BasicXmlAttributeDescriptor {
//
//                })
        return arrayOf()
    }

    override fun getAttributeDescriptor(name: String?, tag: XmlTag?): XmlAttributeDescriptor? {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class HtlAttributeDescriptor : HtmlAttributeDescriptorImpl(null, false) {

}