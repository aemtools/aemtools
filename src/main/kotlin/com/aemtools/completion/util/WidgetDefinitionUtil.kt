package com.aemtools.completion.util

import com.aemtools.completion.model.psi.PsiWidgetDefinition
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro_Troynikov
 */
object WidgetDefinitionUtil {

  /**
   * Try to extract [PsiWidgetDefinition] instance from given
   * [PsiElement] object
   * @param element element to look into
   * @return PsiWidgetDefinition object or *null*
   */
  fun extract(element: PsiElement): PsiWidgetDefinition? {
    val tag = PsiXmlUtil.extractTag(element) ?: return null

    return extractDefinition(tag, element as XmlElement)
  }

  private fun extractDefinition(tag: XmlTag, selectedElement: XmlElement): PsiWidgetDefinition {
    val attributes = tag.attributes
    return PsiWidgetDefinition.create(attributes, selectedElement)
  }

}

