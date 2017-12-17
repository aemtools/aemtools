package com.aemtools.lang.util

import com.aemtools.common.constant.const
import com.aemtools.common.constant.const.SLY_TAG
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.getHtmlFile
import com.aemtools.common.util.getPsi
import com.aemtools.common.util.hasParentOfType
import com.aemtools.common.util.isHtlAttributeName
import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlAssignmentValue
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlExpression
import com.aemtools.lang.htl.psi.HtlHel
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

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
  return this.hasParentOfType(HtlContextExpression::class.java)
      && !this.hasParentOfType(HtlAssignmentValue::class.java)
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

/**
 * Check if current [XmlTag] is sly tag.
 * @receiver [XmlTag]
 * @return *true* if current tag is "sly" tag, *false* otherwise
 */
fun XmlTag.isSlyTag(): Boolean = this.name == SLY_TAG

/**
 * Extract [HtlHtlEl] from current attribute.
 * @receiver [XmlAttribute]
 * @return first [HtlHtlEl] element from current attribute, *null* if no such element found
 */
fun XmlAttribute.extractHtlHel(): HtlHtlEl? {
  val htlFile = containingFile?.viewProvider?.getPsi(HtlLanguage)
      ?: return null
  val valueElement = valueElement ?: return null
  val helStart = htlFile.findElementAt(valueElement.textOffset + 1)
  return helStart.findParentByType(HtlHtlEl::class.java)
}

/**
 * Get [HtlPsiFile] from current [PsiFile].
 *
 * @receiver [PsiFile]
 * @return psi file for [HtlLanguage] language, *null* if no such file available
 */
fun PsiFile.getHtlFile(): HtlPsiFile? = getPsi(HtlLanguage) as? HtlPsiFile

/**
 * Check if current [PsiFile] is [HtlPsiFile].
 *
 * @receiver [PsiFile]
 * @return *true* if current file is htl file, *false* otherwise
 */
fun PsiFile.isHtlFile(): Boolean = getHtlFile() != null

/**
 * Check if current [XmlAttribute] is `data-sly-use` attribute.
 *
 * @receiver [XmlAttribute]
 * @return *true* if current attribute is data-sly-use, *false* otherwise
 */
fun XmlAttribute.isDataSlyUse(): Boolean = this.name.startsWith("${const.htl.DATA_SLY_USE}.")
    || this.name == const.htl.DATA_SLY_USE

/**
 * Check if current [XmlAttribute] is Htl attribute.
 *
 * @return __true__ if current element is Htl attribute
 */
fun XmlAttribute.isHtlAttribute(): Boolean = this.name.isHtlAttributeName()

/**
 * Check if current element is Htl attribute which declares some variable.
 *
 * e.g.
 *
 * ```
 * data-sly-use.bean -> true
 * data-sly-use -> false
 * data-sly-list -> true
 * ```
 *
 * @receiver [XmlAttribute]
 * @return *true* if current element declares some variable
 */
fun XmlAttribute.isHtlDeclarationAttribute(): Boolean =
    with(this.name) {
      when {
        startsWith(const.htl.DATA_SLY_USE) && length > const.htl.DATA_SLY_USE.length -> true
        startsWith(const.htl.DATA_SLY_TEST) && length > const.htl.DATA_SLY_TEST.length -> true
        startsWith(const.htl.DATA_SLY_TEMPLATE) -> true
        startsWith(const.htl.DATA_SLY_LIST) -> true
        startsWith(const.htl.DATA_SLY_REPEAT) -> true
        else -> false
      }
    }

/**
 * Check if current attribute is "local" declaration attribute
 * (i.e. may be referenced only from current file).
 *
 * @receiver [XmlAttribute]
 * @return *true* if current attribute is local declaration attribute
 */
fun XmlAttribute.isHtlLocalDeclarationAttribute(): Boolean =
    isHtlDeclarationAttribute() && !isHtlGlobalDeclarationAttribute()

/**
 * Check if current attribute is "global" declaration attribute
 * (i.e. may be referenced from outside current file).
 *
 * @receiver [XmlAttribute]
 * @return *true* if current element is global declaration attribute
 */
fun XmlAttribute.isHtlGlobalDeclarationAttribute(): Boolean =
    with(this.name) {
      when {
        startsWith(const.htl.DATA_SLY_TEMPLATE) -> true
        else -> false
      }
    }

/**
 * Extract template parameters from current [XmlAttribute]
 * e.g.
 *
 * ```
 *   data-sly-template.myTemplate="${@ param1, param2}"
 * ```
 *
 * will return list of "param1" and "param2".
 *
 * @receiver [XmlAttribute]
 * @return list of parameter names, empty list if no parameters were declared
 * or in case if the attribute is not `data-sly-template`
 */
fun XmlAttribute.extractTemplateParameters(): List<String> {
  if (!this.name.startsWith(const.htl.DATA_SLY_TEMPLATE)) {
    return listOf()
  }

  val htlHel = this.extractHtlHel() ?: return listOf()

  return htlHel.findChildrenByType(HtlVariableName::class.java)
      .filter(HtlVariableName::isOption).map { it.text }
}


/**
 * Extract Htl attributes from given [XmlAttribute] collection.
 *
 * @receiver [List] of [XmlAttribute] objects
 * @return new collection with only Htl attributes
 */
fun List<XmlAttribute>.htlAttributes(): List<XmlAttribute> =
    filter { it.isHtlAttribute() }

/**
 * Resolves the class of variable declared in current [XmlAttribute] element.
 *
 * e.g.
 *
 * ```
 * data-sly-use.bean="com.my.package.BeanClass" -> "com.my.package.BeanClass
 * data-sly-use.bean="${'com.my.package.BeanClass' @ param1='value'}" -> "com.my.package.BeanClass"
 *
 * data-sly-use.bean="javascript-file.js"
 * ->
 * ???
 * ```
 *
 * @receiver [XmlAttribute]
 * @return full qualified class name, or _null_ in case if resolution is not possible
 */
@Deprecated("To be removed")
fun XmlAttribute.resolveUseClass(): String? {
  val attributeValue = valueElement?.value ?: return null
  return when {
    attributeValue.matches(Regex("[\\w.]+")) -> attributeValue
    attributeValue.indexOf("\${") != -1 -> extractBeanNameFromEl(attributeValue)
    else -> null
  }
}

@Deprecated("To be removed")
private fun extractBeanNameFromEl(el: String): String? {
  val start = el.indexOf("'") + 1
  val end = el.indexOf("'", start + 1)
  if (start != -1 && end != -1) {
    return el.substring(start, end)
  }
  return null
}
