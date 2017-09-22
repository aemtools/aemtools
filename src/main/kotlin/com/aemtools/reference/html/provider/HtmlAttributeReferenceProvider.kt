package com.aemtools.reference.html.provider

import com.aemtools.completion.util.isHtlAttribute
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.impl.source.xml.XmlAttributeImpl
import com.intellij.psi.impl.source.xml.XmlAttributeReference
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references for Htl attributes to make it possible to trigger `Quick Documentation` action on them.
 *
 * @author Dmytro Troynikov
 */
object HtmlAttributeReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement,
                                      context: ProcessingContext): Array<out PsiReference> {
    val attr = element as XmlAttribute

    if (attr.isHtlAttribute()) {
      return arrayOf(XmlAttributeReferenceWrapper(attr as XmlAttributeImpl))
    }

    return arrayOf()
  }

  /**
   * Xml attribute reference wrapper.
   */
  class XmlAttributeReferenceWrapper(val xmlAttribute: XmlAttributeImpl) : XmlAttributeReference(xmlAttribute) {
    override fun resolve() = xmlAttribute

    override fun getRangeInElement(): TextRange {
      val range = with(xmlAttribute.name) {
        if (contains(".")) {
          TextRange(0, indexOf("."))
        } else {
          TextRange(0, length)
        }
      }
      return range
    }
  }
}

