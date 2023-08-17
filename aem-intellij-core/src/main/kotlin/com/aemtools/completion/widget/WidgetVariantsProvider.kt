package com.aemtools.completion.widget

import com.aemtools.common.completion.lookupElement
import com.aemtools.common.constant.const
import com.aemtools.common.util.findParentByType
import com.aemtools.completion.model.WidgetMember
import com.aemtools.completion.model.psi.PsiWidgetDefinition
import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlToken

/**
 * @author Dmytro Primshyts.
 */
object WidgetVariantsProvider {

  val DEFAULT_ATTRIBUTES = listOf("jcr:primaryType", const.XTYPE)
  val JCR_PRIMARY_TYPE_VALUES = listOf("nt:unstructured",
      "cq:Widget",
      "cq:WidgetCollection",
      "cq:Dialog",
      "cq:TabPanel",
      "cq:Panel")

  /**
   * Generate variants for given completion parameters and widget definition.
   *
   * @param parameters the completion parameters
   * @param widgetDefinition the widget definition
   * @return collection of lookup elements
   */
  fun generateVariants(parameters: CompletionParameters,
                       widgetDefinition: PsiWidgetDefinition?)
      : Collection<LookupElement> {

    val currentElement = parameters.position as XmlToken
    val currentPositionType = currentElement.tokenType.toString()

    if (widgetXtypeUnknown(widgetDefinition)) {
      when (currentPositionType) {
        const.xml.XML_ATTRIBUTE_NAME -> {
          val collection = genericForName()
          if (widgetDefinition != null) {
            return collection.filter {
              widgetDefinition
                  .getFieldValue(it.lookupString) == null
            }
          } else {
            return collection
          }
        }
        const.xml.XML_ATTRIBUTE_VALUE -> {
          return when (currentElement.findParentByType(XmlAttribute::class.java)?.name) {
            const.XTYPE -> variantsForXTypeValue(currentElement)
            const.JCR_PRIMARY_TYPE -> variantsForJcrPrimaryType()
            else -> variantsForValue()
          }
        }
        else -> return listOf()
      }
    }

    when (currentPositionType) {
      const.xml.XML_ATTRIBUTE_NAME -> return variantsForName(widgetDefinition as PsiWidgetDefinition)
          .filter { it ->
            widgetDefinition.getFieldValue(it.lookupString) == null
          }
      const.xml.XML_ATTRIBUTE_VALUE -> {
        return when (currentElement.findParentByType(XmlAttribute::class.java)?.name) {
          const.XTYPE -> variantsForXTypeValue(currentElement)
          const.JCR_PRIMARY_TYPE -> variantsForJcrPrimaryType()
          else -> variantsForValue()
        }
      }
    }
    return listOf()
  }

  private fun widgetXtypeUnknown(widgetDefinition: PsiWidgetDefinition?): Boolean
      = widgetDefinition?.getFieldValue(const.XTYPE) == null

  private fun genericForName(): Collection<LookupElement> {
    return DEFAULT_ATTRIBUTES.map {
      lookupElement(it)
          .withInsertHandler(XmlAttributeInsertHandler())
    }
  }

  private fun variantsForXTypeValue(currentToken: XmlToken): Collection<LookupElement> {
    val widgetDocRepository = ServiceFacade.getWidgetRepository()

    val text = currentToken.text
    val query: String? = if (text.contains(const.IDEA_STRING_CARET_PLACEHOLDER)) {
      text.substring(0, text.indexOf(const.IDEA_STRING_CARET_PLACEHOLDER))
    } else {
      text
    }
    val xtypes: List<String> = widgetDocRepository.findXTypes(query)
    return xtypes.map { lookupElement(it) }
  }

  private fun variantsForJcrPrimaryType(): Collection<LookupElement> =
      JCR_PRIMARY_TYPE_VALUES.map { lookupElement(it) }

  /**
   * Give variants for attribute name
   */
  private fun variantsForName(widgetDefinition: PsiWidgetDefinition): Collection<LookupElement> {
    val widgetDocRepository = ServiceFacade.getWidgetRepository()

    val xtype = widgetDefinition.getFieldValue("xtype") ?: return listOf()

    val doc = widgetDocRepository.findByXType(xtype) ?: return listOf()

    val result = doc.members
        .filter { it.memberType != WidgetMember.MemberType.PUBLIC_METHOD }
        .map {
          lookupElement(it.name)
              .withTypeText(it.type)
              .withInsertHandler(XmlAttributeInsertHandler())
        }

    return result
  }

  /**
   * Give variants for attribute value
   */
  private fun variantsForValue(): Collection<LookupElement> = listOf()


}
