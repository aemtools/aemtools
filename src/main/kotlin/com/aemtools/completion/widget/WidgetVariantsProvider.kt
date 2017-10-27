package com.aemtools.completion.widget

import com.aemtools.completion.model.WidgetMember
import com.aemtools.completion.model.psi.PsiWidgetDefinition
import com.aemtools.completion.util.PsiXmlUtil
import com.aemtools.constant.const
import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.xml.XmlToken

/**
 * @author Dmytro_Troynikov.
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
            return collection.filter { it ->
              widgetDefinition
                  .getFieldValue(it.lookupString) == null
            }
          } else {
            return collection
          }
        }
        const.xml.XML_ATTRIBUTE_VALUE -> {
          return when (PsiXmlUtil.nameOfAttribute(currentElement)) {
            const.XTYPE -> variantsForXTypeValue(currentElement)
            const.JCR_PRIMARY_TYPE -> variantsForJcrPrimaryType()
            else -> variantsForValue(parameters, widgetDefinition as PsiWidgetDefinition)
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
        return when (PsiXmlUtil.nameOfAttribute(currentElement)) {
          const.XTYPE -> variantsForXTypeValue(currentElement)
          const.JCR_PRIMARY_TYPE -> variantsForJcrPrimaryType()
          else -> variantsForValue(parameters, widgetDefinition as PsiWidgetDefinition)
        }
      }
    }
    return listOf()
  }

  private fun widgetXtypeUnknown(widgetDefinition: PsiWidgetDefinition?): Boolean
      = widgetDefinition?.getFieldValue(const.XTYPE) == null

  private fun genericForName(): Collection<LookupElement> {
    return DEFAULT_ATTRIBUTES.map { it ->
      LookupElementBuilder.create(it)
          .withInsertHandler(XmlAttributeInsertHandler())
    }
  }

  private fun variantsForXTypeValue(currentToken: XmlToken): Collection<LookupElement> {
    val widgetDocRepository = ServiceFacade.getWidgetRepository()
    val query: String? = PsiXmlUtil.removeCaretPlaceholder(currentToken.text)
    val xtypes: List<String> = widgetDocRepository.findXTypes(query)
    return xtypes.map { it -> LookupElementBuilder.create(it) }
  }

  private fun variantsForJcrPrimaryType(): Collection<LookupElement> =
      JCR_PRIMARY_TYPE_VALUES.map { it -> LookupElementBuilder.create(it) }

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
          LookupElementBuilder.create(it.name)
              .withTypeText(it.type)
              .withInsertHandler(XmlAttributeInsertHandler())
        }

    return result
  }

  /**
   * Give variants for attribute value
   */
  private fun variantsForValue(parameters: CompletionParameters,
                               widgetDefinition: PsiWidgetDefinition): Collection<LookupElement> = listOf()

}
