package com.aemtools.documentation.html

import com.aemtools.completion.model.htl.HtlAttributeIdentifierDescription
import com.aemtools.completion.model.htl.HtlAttributeMetaInfo
import com.aemtools.completion.model.htl.HtlAttributeValueDescription
import com.aemtools.completion.util.isHtlAttribute
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.htlAttribute
import com.aemtools.service.ServiceFacade
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro_Troynikov
 */
class HtlAttributesDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (htlAttribute.accepts(originalElement)) {
            val parentAttribute = originalElement?.parent as? XmlAttribute
                ?: return super.generateDoc(element, originalElement)

            val name = parentAttribute.htlAttributeName()
                ?: return super.generateDoc(element, originalElement)

            return ServiceFacade.getHtlAttributesRepository()
                    .getAttributesData()
                    .find { it.name == name }
                    .let {
                        if (it == null) {
                            super.generateDoc(element, originalElement)
                        } else {
                            generateDocumentation(it)
                        }
                    }
        } else {
            return super.generateDoc(element, originalElement)
        }
    }

    private fun generateDocumentation(info: HtlAttributeMetaInfo): String = with(info) {
        val builder= StringBuilder()
        with(builder) {
            append("<h2>$name</h2>")
            append(descriptionBlock(general))
            append(elementBlock(element))
            append(elementContentBlock(elementContent))
            append(attributeValueBlock(attributeValue))
            append(attributeIdentifierBlock(attributeIdentifier))
            append(footer(info))
        }
        builder.toString()
    }

    private fun attributeIdentifierBlock(value: HtlAttributeIdentifierDescription?): String {
        return if (value != null && value.isNotEmpty()) {
            val builder = StringBuilder()
            with(builder) {
                append("<b>Attribute identifier:</b><br>")
                append(renderBlock(value.required) {" - required: ${value.required}<br>"})
                append(renderBlock(value.description) {" - description: ${value.description}<br>"})
            }
            builder.toString()
        } else {
            ""
        }
    }

    private fun attributeValueBlock(value: HtlAttributeValueDescription?): String {
        val strBuilder = StringBuilder()
        if (value != null && value.isNotEmpty()) {
            with(strBuilder) {
                append("<b>Attribute value:</b><br>")
                append(renderBlock(value.required) { " - required: ${value.required}<br>" })
                append(renderBlock(value.printType()) { " - type: ${value.printType()}<br>" })
                append(renderBlock(value.description) { " - description: ${value.description}<br>" })
            }

        }
        return strBuilder.toString()
    }

    private fun renderBlock(value: String?, unit: () -> String): String {
        return if (value != null) {
            unit.invoke()
        } else {
            ""
        }
    }

    private fun elementContentBlock(value: String?): String = if (value != null) {
        "<b>Content of element:</b> $value<br>"
    } else {
        ""
    }

    private fun elementBlock(value: String?): String = if (value != null) {
        "<b>Element:</b> $value<br>"
    } else {
        ""
    }

    private fun footer(info: HtlAttributeMetaInfo): String {
        return "<br><br>See also: <a href=\"${info.link}\">Htl Specification</a>"
    }

    private fun descriptionBlock(description: String): String {
        return "<b>Description:</b> $description<br> "
    }

    private fun XmlAttribute.htlAttributeName(): String? {
        if (!isHtlAttribute()) {
            return null
        }

        return if (name.contains(".")) {
            name.substring(0, name.indexOf("."))
        } else {
            name
        }
    }
}
