package com.aemtools.completion.model.psi

import com.aemtools.completion.util.findParentByType
import com.aemtools.constant.const
import com.aemtools.util.OpenApiUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlElement
import java.util.LinkedHashMap
import java.util.LinkedList

/**
 * @author Dmytro_Troynikov
 */
data class PsiWidgetDefinition constructor(
    val fields: LinkedHashMap<String, String?>,
    val originalAttributes: LinkedList<XmlAttribute>,
    val selectedAttribute: SelectedAttribute?
) {

  companion object {

    /**
     * Builder method for [PsiWidgetDefinition].
     *
     * @param attributes list of xml attributes
     * @param selectedElement selected xml element
     * @return psi widget definition instance
     */
    fun create(attributes: Array<XmlAttribute>,
               selectedElement: XmlElement): PsiWidgetDefinition {
      val fields = LinkedHashMap<String, String?>()
      val originalAttributes = LinkedList<XmlAttribute>()

      attributes.forEach {
        val name = it.name
        val value = it.value
        originalAttributes.add(it)
        fields.put(name, value)
      }

      return PsiWidgetDefinition(fields, originalAttributes,
          tryExtractSelectedAttribute(selectedElement))
    }

    private fun tryExtractSelectedAttribute(element: XmlElement): SelectedAttribute? {
      val selectedXmlAttribute = element.findParentByType(XmlAttribute::class.java) ?: return null

      val attrName = SelectedString.create(selectedXmlAttribute.name)
          ?: return null
      val attrValue = SelectedString.create(selectedXmlAttribute.value)

      return SelectedAttribute(
          attrName.value,
          attrValue?.value,
          isTheElementSelected(selectedXmlAttribute.nameElement, element),
          isTheElementSelected(selectedXmlAttribute.valueElement, element)
      )
    }

    /**
     * The element is selected if it lays within the selection or it has the cursor in it
     */
    private fun isTheElementSelected(element: PsiElement?, targetElement: PsiElement): Boolean {
      if (element == null) {
        return false
      }
      return (OpenApiUtil.isCurrentThreadIsDispatch() && OpenApiUtil.isCurrentElementSelected(element))
          || targetElement.text == (element as? XmlAttributeValueImpl)?.value
    }
  }

  /**
   * Get value for field by name.
   *
   * @param fieldName the field's name
   * @return field's value, *null* if no field or value present
   */
  fun getFieldValue(fieldName: String): String? {
    return fields[fieldName]
  }

  /**
   * Check if xtype is selected in current [PsiWidgetDefinition].
   *
   * @return *true* if xtype is selected, *false* otherwise
   */
  fun isXtypeValueSelected(): Boolean {
    if (selectedAttribute == null) {
      return false
    } else {
      return selectedAttribute.name == const.XTYPE && selectedAttribute.valueSelected
    }
  }

}
