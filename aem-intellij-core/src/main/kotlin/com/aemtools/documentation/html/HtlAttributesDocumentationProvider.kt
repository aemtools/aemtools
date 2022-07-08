package com.aemtools.documentation.html

import com.aemtools.completion.model.htl.HtlAttributeIdentifierDescription
import com.aemtools.completion.model.htl.HtlAttributeMetaInfo
import com.aemtools.completion.model.htl.HtlAttributeValueDescription
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.htlAttribute
import com.aemtools.lang.util.htlAttributeName
import com.aemtools.service.ServiceFacade
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Primshyts
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
    buildString {
      append("<h2>$name</h2>")
      append(descriptionBlock(general))
      append(elementBlock(element))
      append(elementContentBlock(elementContent))
      append(attributeValueBlock(attributeValue))
      append(attributeIdentifierBlock(attributeIdentifier))
      append(optionsBlock(options))
      append(scopeBlock(scope))
      append(footer(info))
    }
  }

  private fun attributeIdentifierBlock(value: HtlAttributeIdentifierDescription?): String {
    return if (value != null && value.isNotEmpty()) {
      buildString {
        append("<b>Attribute identifier:</b><br>")
        append(renderBlock(value.required) { " - required: ${value.required}<br>" })
        append(renderBlock(value.description) { " - description: ${value.description}<br>" })
      }
    } else {
      ""
    }
  }

  private fun attributeValueBlock(value: HtlAttributeValueDescription?): String =
      if (value != null && value.isNotEmpty()) {
        buildString {
          append("<b>Attribute value:</b><br>")
          append(renderBlock(value.required) { " - required: ${value.required}<br>" })
          append(renderBlock(value.printType()) { " - type: ${value.printType()}<br>" })
          append(renderBlock(value.description) { " - description: ${value.description}<br>" })
        }
      } else {
        ""
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
    return "<b>Description:</b> $description<br>"
  }

  private fun scopeBlock(scope: String?): String = if (scope != null) {
    "<b>Scope:</b> $scope<br>"
  } else {
    ""
  }

  private fun optionsBlock(options: List<HtlOption>?): String {
    return if (options != null && options.isNotEmpty()) {
      buildString {
        append("<b>Options:</b><br>")
        options.forEach {
          append(" - <strong>${it.name}</strong>: ${it.description}<br>")
        }
      }
    } else {
      ""
    }
  }
}
