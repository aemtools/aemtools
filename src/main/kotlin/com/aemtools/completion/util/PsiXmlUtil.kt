package com.aemtools.completion.util

import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.constant.const.SLY_TAG
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.util.isHtlAttributeName
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * Searches for children by type.
 *
 * @receiver [PsiElement]
 * @see [PsiTreeUtil.findChildrenOfType]
 */
fun <T : PsiElement> PsiElement?.findChildrenByType(type: Class<T>): Collection<T> {
  return PsiTreeUtil.findChildrenOfType(this, type)
}

/**
 * Searches for parent PSI element of specified class.
 *
 * @receiver [PsiElement]
 * @see [PsiTreeUtil.findFirstParent]
 */
@Suppress("UNCHECKED_CAST")
fun <T : PsiElement> PsiElement?.findParentByType(type: Class<T>): T? {
  return PsiTreeUtil.findFirstParent(this, Conditions.instanceOf(type)) as? T?
}

private fun <T : PsiElement> PsiElement?.findParentsByType(type: Class<T>): List<T> {
  var element: T? = null
  return generateSequence {
    val next = (this ?: element).findParentByType(type)
    element = next
    element
  }
      .toList()
}

/**
 * Search for parent which is of given type and satisfies given predicate.
 *
 * @param type the type of parent
 * @param predicate the predicate
 * @receiver [PsiElement]
 * @return the element
 */
fun <T : PsiElement> PsiElement?.findParentByType(type: Class<T>, predicate: (T) -> Boolean): T? =
    this.findParentsByType(type).firstOrNull { predicate.invoke(it)}

/**
 * Check if current [PsiElement] has parent of specified class.
 *
 * @receiver [PsiElement]
 * @return *true* if current element has parent of specified type, *false* otherwise
 */
fun <T : PsiElement> PsiElement?.hasParent(type: Class<T>): Boolean =
    this.findParentByType(type) != null

/**
 * Check if current [PsiElement] has child of specified type.
 * @param type type of child
 * @receiver nullable [PsiElement]
 * @return *true* if current element has one or more children of specified type
 */
fun <T : PsiElement> PsiElement?.hasChild(type: Class<T>): Boolean =
    this.findChildrenByType(type).isNotEmpty()

/**
 * Check if current [XmlTag] is sly tag.
 * @receiver [XmlTag]
 * @return *true* if current tag is "sly" tag, *false* otherwise
 */
fun XmlTag.isSlyTag(): Boolean = this.name == SLY_TAG

/**
 * Check if current [XmlTag] contains at least one attribute matched by
 * gived "matcher" function.
 *
 * @param matcher matcher function
 * @return *true* if current tag has matching attribute
 */
infix fun XmlTag.hasAttribute(matcher: XmlAttributeMatcher): Boolean =
    attributes.any(matcher)

typealias XmlAttributeMatcher = (attribute: XmlAttribute) -> Boolean

/**
 * Create "name&value" xml attribute matcher.
 *
 * @param name the name of attribute
 * @param value the value of attribute
 * @return xml attribute matcher
 */
fun xmlAttributeMatcher(name: String, value: String? = null): XmlAttributeMatcher =
    {
      it.name == name
      && (value == null || it.value == value)
    }

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

/**
 * Check if current [XmlAttribute] is `data-sly-use` attribute.
 *
 * @receiver [XmlAttribute]
 * @return *true* if current attribute is data-sly-use, *false* otherwise
 */
fun XmlAttribute.isDataSlyUse(): Boolean = this.name.startsWith("$DATA_SLY_USE.")
    || this.name == DATA_SLY_USE

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
        startsWith(DATA_SLY_USE) && length > DATA_SLY_USE.length -> true
        startsWith(DATA_SLY_TEST) && length > DATA_SLY_TEST.length -> true
        startsWith(DATA_SLY_TEMPLATE) -> true
        startsWith(DATA_SLY_LIST) -> true
        startsWith(DATA_SLY_REPEAT) -> true
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
        startsWith(DATA_SLY_TEMPLATE) -> true
        else -> false
      }
    }

/**
 * Extract list of Htl variable declarations from current [XmlAttribute] collection.
 * @receiver [Collection] of [XmlAttribute] objects
 * @return collection of [HtlVariableDeclaration] elements
 */
fun List<XmlAttribute>.extractDeclarations(): List<HtlVariableDeclaration> {
  return filter { it.isHtlDeclarationAttribute() }
      .flatMap {
        HtlVariableDeclaration.create(it)
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
  if (!this.name.startsWith(DATA_SLY_TEMPLATE)) {
    return listOf()
  }

  val htlHel = this.extractHtlHel() ?: return listOf()

  return htlHel.findChildrenByType(HtlVariableName::class.java)
      .filter(HtlVariableName::isOption).map { it.text }
}

/**
 * Extract [TemplateDefinition] from current [XmlAttribute].
 *
 * @receiver [XmlAttribute]
 * @return template definition, _null_ in case if current tag isn't of `data-sly-template` type.
 */
fun XmlAttribute.extractTemplateDefinition(): TemplateDefinition? {
  val name = if (name.contains(".")) {
    name.substring(name.indexOf(".") + 1)
  } else {
    ""
  }

  val params = extractTemplateParameters()

  return TemplateDefinition(containingFile.virtualFile?.path, name, params)
}

/**
 * Extract text range of name element.
 *
 * @receiver [XmlAttribute]
 * @return text range of name element
 */
fun XmlAttribute.nameRange(): TextRange = this.nameElement.textRange

/**
 * Extract text range of value element.
 *
 * @receiver [XmlAttribute]
 * @return text range of value element
 */
fun XmlAttribute.valueRange(): TextRange? = this.valueElement?.textRange
