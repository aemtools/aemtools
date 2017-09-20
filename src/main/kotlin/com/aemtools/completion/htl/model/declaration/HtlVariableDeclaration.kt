package com.aemtools.completion.htl.model.declaration

import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.extractItemAndItemListNames
import com.aemtools.completion.util.extractTemplateDefinition
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.htlVariableName
import com.aemtools.completion.util.isOption
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.lang.htl.icons.HtlIcons.DATA_SLY_LIST_ICON
import com.aemtools.lang.htl.icons.HtlIcons.DATA_SLY_REPEAT_ICON
import com.aemtools.lang.htl.icons.HtlIcons.HTL_FILE_ICON
import com.aemtools.lang.htl.icons.HtlIcons.LIST_HELPER_ICON
import com.aemtools.lang.htl.icons.HtlIcons.REPEAT_HELPER_ICON
import com.aemtools.lang.htl.icons.HtlIcons.SLY_TEST_VARIABLE_ICON
import com.aemtools.lang.htl.icons.HtlIcons.SLY_USE_VARIABLE_ICON
import com.aemtools.lang.htl.icons.HtlIcons.TEMPLATE_PARAMETER_ICON
import com.aemtools.lang.htl.psi.HtlVariableName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.xml.XmlAttribute

/**
 * Represents the name of variable and the [XmlAttribute] in which it was declared.
 *
 * @author Dmytro_Troynikov
 */
open class HtlVariableDeclaration internal constructor(
    /**
     * Declaration [XmlAttribute].
     */
    val xmlAttribute: XmlAttribute,
    /**
     * The name of variable.
     */
    val variableName: String,
    /**
     * The type of Htl declaration attribute.
     */
    val attributeType: DeclarationAttributeType,
    /**
     * The [DeclarationType] of current variable.
     */
    val type: DeclarationType = DeclarationType.VARIABLE) {

  /**
   * Convert current [HtlVariableDeclaration] into [LookupElement].
   * @return lookup xmlAttribute
   */
  fun toLookupElement(): LookupElement {
    var result = LookupElementBuilder.create(variableName)

    when (attributeType) {
      DeclarationAttributeType.DATA_SLY_USE -> {
        val varClass = xmlAttribute.resolveUseClass()
        result = result.withTypeText("Sly Use Variable")
            .withIcon(SLY_USE_VARIABLE_ICON)
        if (!varClass.isNullOrEmpty()) {
          result = result.withTailText("($varClass)", true)
        }
      }
      DeclarationAttributeType.DATA_SLY_TEST -> {
        val varClass = xmlAttribute.resolveUseClass()
        result = result.withTypeText("Sly Test Variable")
            .withIcon(SLY_TEST_VARIABLE_ICON)
        if (!varClass.isNullOrEmpty()) {
          result = result.withTailText("($varClass)")
        }
      }
      DeclarationAttributeType.DATA_SLY_LIST -> {
        result = result.withTypeText("Data Sly List")
            .withIcon(DATA_SLY_LIST_ICON)
      }
      DeclarationAttributeType.DATA_SLY_REPEAT -> {
        result = result.withTypeText("Data Sly Repeat")
            .withIcon(DATA_SLY_REPEAT_ICON)
      }
      DeclarationAttributeType.DATA_SLY_TEMPLATE_PARAMETER -> {
        result = result.withTypeText("Template Parameter")
            .withIcon(TEMPLATE_PARAMETER_ICON)
      }
      DeclarationAttributeType.DATA_SLY_TEMPLATE -> {
        result = result.withTypeText("HTL Template")
            .withIcon(HTL_FILE_ICON) // todo find more appropriate icon
      }
      DeclarationAttributeType.LIST_HELPER -> {
        result = result.withTypeText("List Helper")
            .withIcon(LIST_HELPER_ICON)
      }
      DeclarationAttributeType.REPEAT_HELPER -> {
        result = result.withTypeText("Repeat Helper")
            .withIcon(REPEAT_HELPER_ICON)
      }
    }
    return result
  }

  companion object {

    /**
     * Builder method for [HtlVariableDeclaration].
     *
     * @param attribute the declaration attribute
     * @return list of htl variable declaration objects spawned by given attribute
     */
    fun create(attribute: XmlAttribute): List<HtlVariableDeclaration> {
      val htlAttributeName = attribute.htlAttributeName()
      val htlVariableName = attribute.htlVariableName()
      return when {
        htlAttributeName == DATA_SLY_USE
            && htlVariableName != null -> {
          listOf(
              HtlUseVariableDeclaration(
                  attribute,
                  htlVariableName,
                  DeclarationAttributeType.DATA_SLY_USE
              )
          )
        }
        htlAttributeName == DATA_SLY_TEST
            && htlVariableName != null -> {
          listOf(
              HtlVariableDeclaration(
                  attribute,
                  htlVariableName,
                  DeclarationAttributeType.DATA_SLY_TEST,
                  DeclarationType.VARIABLE
              )
          )
        }
        htlAttributeName == DATA_SLY_LIST -> {
          val (item, itemList) = extractItemAndItemListNames(attribute.name)

          listOf(
              HtlVariableDeclaration(
                  attribute,
                  item,
                  DeclarationAttributeType.DATA_SLY_LIST,
                  DeclarationType.ITERABLE
              ),
              HtlListHelperDeclaration.createForList(
                  attribute,
                  itemList
              )
          )
        }
        htlAttributeName == DATA_SLY_REPEAT -> {
          val (item, itemList) = extractItemAndItemListNames(attribute.name)

          listOf(
              HtlVariableDeclaration(
                  attribute,
                  item,
                  DeclarationAttributeType.DATA_SLY_REPEAT,
                  DeclarationType.ITERABLE
              ),
              HtlListHelperDeclaration.createForRepeat(
                  attribute, itemList
              )
          )
        }

        htlAttributeName == DATA_SLY_TEMPLATE -> {
          val templateParameters = extractTemplateParams(attribute)

          val templateName = attribute.htlVariableName()
          val templateDefinition = attribute.extractTemplateDefinition()
          if (templateName != null && templateDefinition != null) {
            templateParameters + HtlTemplateDeclaration(
                templateDefinition,
                templateParameters,
                attribute,
                templateName
            )
          } else {
            templateParameters
          }
        }

        else -> listOf()
      }
    }

    private fun extractTemplateParams(attribute: XmlAttribute): List<HtlTemplateParameterDeclaration> =
        attribute.extractHtlHel().findChildrenByType(HtlVariableName::class.java)
            .filter(HtlVariableName::isOption)
            .map {
              HtlTemplateParameterDeclaration(it,
                  attribute,
                  it.text)
            }

  }

}
