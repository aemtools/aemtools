package com.aemtools.documentation.component

import com.aemtools.completion.small.patterns.CqComponentPatterns.attributeInCqComponent
import com.aemtools.service.repository.inmemory.CqComponentRepository
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * Documentation provider for attributes of the node with jcr:primaryType="cq:Component".
 *
 * @author Kostiantyn Diachenko
 */
class CqComponentDocumentationProvider : AbstractDocumentationProvider() {
  override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
    if (originalElement == null) {
      return super.generateDoc(element, originalElement)
    }

    if (attributeInCqComponent.accepts(originalElement)) {
      val parentAttribute = originalElement.parent as? XmlAttribute
          ?: return super.generateDoc(element, originalElement)

      return CqComponentRepository.getNodeProperties()
          .find { it.name == parentAttribute.name }
          .let {
            if (it == null) {
              super.generateDoc(element, originalElement)
            } else {
              generateDocumentation(it)
            }
          }
    }
    return super.generateDoc(element, originalElement)
  }

  private fun generateDocumentation(info: CqComponentRepository.CqComponentProperty): String {
    return buildString {
      append("<h2>${info.name}</h2>")
      append("<b>Type: </b>${info.type}<br/>")
      append("<b>Description: </b>${info.description}")
    }
  }
}
