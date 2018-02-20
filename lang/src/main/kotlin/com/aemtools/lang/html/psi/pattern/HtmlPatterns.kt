package com.aemtools.lang.html.psi.pattern

import com.aemtools.lang.htl.psi.pattern.HtlFilePattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiFile
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlTokenType

/**
 * @author Dmytro Troynikov
 */
object HtmlPatterns {

  /**
   * Matches HTML attribute name within HTL file.
   */
  val attributeInHtlFile = PlatformPatterns.psiElement(XmlTokenType.XML_NAME)
      .inside(XmlPatterns.xmlAttribute())
      .inFile(psiFile().with(HtlFilePattern))

  /**
   * Matches attribute of `a` tag within HTL file.
   */
  val aInHtlFile = PlatformPatterns.psiElement(XmlTokenType.XML_NAME)
      .inside(XmlPatterns.xmlAttribute().inside(
          XmlPatterns.xmlTag().withName("a")
      ))
      .inFile(psiFile().with(HtlFilePattern))

  /**
   * Matches value of `x-cq-linkchecker` attribute.
   */
  val valueOfXLinkChecker = PlatformPatterns.psiElement(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN)
      .inside(XmlPatterns.xmlAttribute("x-cq-linkchecker"))
      .inFile(psiFile().with(HtlFilePattern))

}
