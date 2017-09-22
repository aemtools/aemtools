package com.aemtools.lang.htl.psi.util

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlTokenType

/**
 * @author Dmytro Troynikov
 */

/**
 * Check if current [PsiElement] resides within the body of given [XmlTag]
 *  e.g. given element - this, div - the tag to compare against
 *
 *  ```
 *      <div>  <element>  <div> --> true
 *
 *      <div attribute="<element>"> ... </div> --> false
 *
 *      <div></div> <element> --> false
 *  ```
 *
 *  @return __true__ if current element is situated within given tag's body.
 */
fun PsiElement.isWithin(tag: XmlTag): Boolean {
  val composite = tag as CompositeElement
  val tagEnd = composite.findChildByType(XmlTokenType.XML_TAG_END) ?: return false
  val endTagStart = composite.findChildByType(XmlTokenType.XML_END_TAG_START) ?: return false

  val leftBorder = tagEnd.startOffset + 1
  val rightBorder = endTagStart.startOffset

  return textOffset > leftBorder && textOffset < rightBorder
}

/**
 * Check if current [PsiElement] is not within the body of give [XmlTag]
 * (inverted version of [isWithin])
 */
fun PsiElement.isNotWithin(tag: XmlTag): Boolean = !isWithin(tag)

/**
 * Check if current [PsiElement] is part of given [XmlTag]
 *  e.g. given: element - this, div - the tag to check:
 *
 *  ```
 *      <div> <element> </div> --> true
 *      <div attribute="<element>"></div> --> true
 *      <div></div> <element> --> false
 *  ```
 *
 *  @return __true__ in case if current element is part given tag
 */
fun PsiElement.isPartOf(element: XmlElement): Boolean {
  return textOffset > element.textOffset && textOffset < (element.textOffset + element.textLength)
}

/**
 * Check if current element is not part of given element.
 * (inverted version of [isPartOf])
 */
fun PsiElement.isNotPartOf(element: XmlElement): Boolean {
  return !isPartOf(element)
}
