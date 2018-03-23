package com.aemtools.common.util

import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.util.IncludedXmlAttribute

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
    this.findParentsByType(type).firstOrNull { predicate.invoke(it) }

/**
 * Check if current [PsiElement] has parent of specified class.
 *
 * @receiver [PsiElement]
 * @return *true* if current element has parent of specified type, *false* otherwise
 */
fun <T : PsiElement> PsiElement?.hasParentOfType(type: Class<T>): Boolean =
    this.findParentByType(type) != null

/**
 * Check if current [PsiElement] has parent which is evaluates to *true* by given predicate.
 *
 * @param predicate the predicate
 * @receiver [PsiElement]
 * @return *true* if this psi element has element that conforms to given predicate, *false* otherwise
 */
fun <T : PsiElement> T.hasParent(predicate: (element: PsiElement) -> Boolean): Boolean {
  var parent = this.parent
  while (parent != null) {
    if (predicate.invoke(parent)) {
      return true
    }
    parent = parent.parent
  }
  return false
}

/**
 * Check if current [PsiElement] has child of specified type.
 * @param type type of child
 * @receiver nullable [PsiElement]
 * @return *true* if current element has one or more children of specified type
 */
fun <T : PsiElement> PsiElement?.hasChild(type: Class<T>): Boolean =
    this.findChildrenByType(type).isNotEmpty()

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
 * Check if current [XmlAttribute] is doublequoted attributed. e.g.:
 *
 * ```
 * attribute="value" -> true
 * attribute='value' -> false
 * ```
 *
 * @receiver [XmlAttribute]
 * @return *true* if current attribute is doublequoted, *false* otherwise
 */
fun XmlAttribute.isDoubleQuoted() : Boolean {
  return this.valueElement?.text?.startsWith("\"") ?: false
}

/**
 * Extract text range of name element.
 *
 * @receiver [XmlAttribute]
 * @return text range of name element
 */
fun XmlAttribute.nameRange(): TextRange = this.nameElement.textRange

/**
 * Convert current xml attribute to navigable element.
 *
 * @receiver [XmlAttribute]
 * @return navigatable element
 */
fun XmlAttribute.toNavigatable(): IncludedXmlAttribute {
  return IncludedXmlAttribute(this, this.parent)
}
