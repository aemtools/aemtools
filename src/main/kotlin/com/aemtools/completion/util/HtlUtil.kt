package com.aemtools.completion.util

import com.aemtools.completion.htl.model.declaration.DeclarationAttributeType
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.constant.const
import com.aemtools.lang.htl.psi.HtlAssignmentValue
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlExpression
import com.aemtools.lang.htl.psi.HtlHel
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.htl.psi.util.isNotPartOf
import com.aemtools.lang.htl.psi.util.isPartOf
import com.aemtools.lang.htl.psi.util.isWithin
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import java.util.ArrayList

/**
 * Htl related utility methods.
 *
 * @author Dmytro_Troynikov
 */

/**
 * Extract iterable variable and list variable from given attribute name
 * e.g.
 *
 * ```
 * data-sly-list -> item to itemList
 * data-sly-repeat.overriden -> overriden to overridenList
 * ```
 * @return _iterable_ to _list variable_ names pair
 */
fun extractItemAndItemListNames(value: String): Pair<String, String> {
  val item: String = when {
    value.startsWith(const.htl.DATA_SLY_REPEAT) && value.length > const.htl.DATA_SLY_REPEAT.length + 1 -> {
      value.substring(value.lastIndexOf(".") + 1)
    }
    value.startsWith(const.htl.DATA_SLY_LIST) && value.length > const.htl.DATA_SLY_LIST.length + 1 -> {
      value.substring(value.lastIndexOf(".") + 1)
    }
    else -> {
      "item"
    }
  }
  return item to "${item}List"
}

/**
 * Check if current string literal is the main EL's string
 * (e.g. ${'main string' @ param='not main string'})
 *
 * @receiver [HtlStringLiteral]
 * @return *true* if current string is main EL string, *false* otherwise
 */
fun HtlStringLiteral.isMainString(): Boolean {
  val expression = this.findParentByType(HtlExpression::class.java) ?: return false

  return expression.parent is HtlHel
}

/**
 * Check if current variable is "option"
 *
 * ```
 *  ${variable} -> false
 *  ${@ variable} -> true
 * ```
 *
 * @receiver [HtlVariableName]
 * @return *true* if current variable name is "option", *false* otherwise
 */
fun HtlVariableName.isOption(): Boolean {
  return this.hasParent(HtlContextExpression::class.java)
      && !this.hasParent(HtlAssignmentValue::class.java)
}

/**
 * Check if current variable is "option".
 *
 * @receiver [VariableNameMixin]
 * @see [isOption]
 * @return *true* if current variable is "option", *false* otherwise
 */
fun VariableNameMixin.isOption(): Boolean =
    (this as? HtlVariableName)
        ?.isOption()
        ?: false

/**
 * Extract first (top level) [PropertyAccessMixin].
 *
 * @receiver [HtlHtlEl]
 * @return first [PropertyAccessMixin] in current [HtlHtlEl], *null* if no property access mixin present
 */
fun HtlHtlEl.extractPropertyAccess(): PropertyAccessMixin? {
  val propertyAccessItems = findChildrenByType(PropertyAccessMixin::class.java)
  if (propertyAccessItems.isEmpty()) {
    return null
  }
  return propertyAccessItems.first()
}

/**
 * Check if current [HtlHtlEl] element resides within attribute with given name.
 * @param attributeName the name of attribute
 * @receiver [HtlHtlEl]
 * @return __true__ if current element is the value of attribute with given name
 */
fun HtlHtlEl.isInsideOF(attributeName: String): Boolean {
  val html = this.containingFile.getHtmlFile()
      ?: return false
  val attribute = html.findElementAt(this.textOffset - 1)
      .findParentByType(XmlAttribute::class.java) ?: return false

  return attribute.name.startsWith(attributeName)
}

/**
 * Check if current [PsiElement] element resides within attribute with given name.
 *
 * @param attributeName the name of attribute
 * @receiver [PsiElement]
 * @return __true__ if current element si the value of attribute with given name
 */
fun PsiElement.isInsideOf(attributeName: String): Boolean {
  val htlHtlEl = findParentByType(HtlHtlEl::class.java) ?: return false
  return htlHtlEl.isInsideOF(attributeName)
}

/**
 * Collection [HtlVariableDeclaration] element which are applicable for given position
 * @param position the starting position
 * @receiver [List] of [HtlVariableDeclaration] objects
 * @return collection of applicable declarations
 */
fun List<HtlVariableDeclaration>.filterForPosition(position: PsiElement): List<HtlVariableDeclaration> {
  val applicableDeclarations = this.filter {
    when (it.attributeType) {
      DeclarationAttributeType.DATA_SLY_USE ->
        true
      DeclarationAttributeType.DATA_SLY_TEST ->
        true
      DeclarationAttributeType.DATA_SLY_LIST,
      DeclarationAttributeType.LIST_HELPER -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

        position.isWithin(tag)
      }
      DeclarationAttributeType.DATA_SLY_REPEAT,
      DeclarationAttributeType.REPEAT_HELPER -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

        position.isPartOf(tag) && position.isNotPartOf(it.xmlAttribute)
      }
      DeclarationAttributeType.DATA_SLY_TEMPLATE_PARAMETER -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

        position.isWithin(tag)
      }

      DeclarationAttributeType.DATA_SLY_TEMPLATE -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false
        !position.isPartOf(tag)
      }
    }
  }

  val groupedByName = applicableDeclarations.groupBy { it.variableName }
  val result = if (groupedByName.values.find { it.size > 1 } != null) {

    groupedByName.values.flatMap {
      if (it.size == 1) {
        it
      } else {

        val parentTags = position.run {
          val html = position.containingFile.getHtmlFile() ?: return@run listOf<XmlTag>()
          val parent = position.findParentByType(HtlHtlEl::class.java) ?: return@run listOf<XmlTag>()
          val offset = parent.textOffset - 1
          val htmlElement = html.findElementAt(offset)

          var currentElement = htmlElement.findParentByType(XmlTag::class.java)
          val result = ArrayList<XmlTag>()

          while (currentElement != null) {
            result.add(currentElement)
            currentElement = currentElement.prevSibling.findParentByType(XmlTag::class.java)
          }
          result
        }

        val closest = it.minBy {
          val myTag = it.xmlAttribute.findParentByType(XmlTag::class.java)
              ?: return@minBy 100

          val myIndex = parentTags.indexOf(myTag)
          return@minBy if (myIndex > -1) {
            myIndex
          } else {
            100
          }
        } ?: return@flatMap listOf<HtlVariableDeclaration>()

        listOf(closest)
      }
    }

  } else {
    applicableDeclarations
  }
  return result
}

/**
 * Extract Htl attribute name.
 *
 * ```
 * data-sly-use.bean -> "data-sly-use"
 * data-sly-test -> "data-sly-test"
 * class -> null
 * ```
 *
 * @receiver [XmlAttribute]
 * @return the name of Htl attribute, _null_ if current attribute is not Htl one
 */
fun XmlAttribute.htlAttributeName(): String? {
  if (!isHtlAttribute()) {
    return null
  }

  return if (name.contains(".")) {
    name.substring(0, name.indexOf("."))
  } else {
    name
  }
}

/**
 * Extract name of Htl variable declared in current [XmlAttribute]
 *
 * ```
 * data-sly-use.bean -> "bean"
 * data-sly-use -> null
 * ```
 *
 * @receiver [XmlAttribute]
 * @return the name of Htl variable declared in current Htl attribute
 */
fun XmlAttribute.htlVariableName(): String? {
  if (!isHtlDeclarationAttribute()) {
    return null
  }

  return if (name.contains(".")) {
    name.substring(name.indexOf(".") + 1)
  } else {
    null
  }
}
