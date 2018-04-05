package com.aemtools.completion.small.patterns

import com.aemtools.common.util.hasAttribute
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.TreeElementPattern
import com.intellij.patterns.XmlPatterns.xmlFile
import com.intellij.patterns.XmlPatterns.xmlTag
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlTokenType.XML_NAME
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object JcrPatterns {

  /**
   * Matches an attribute of `jcr:root` tag with
   * `jcr:primaryType="cq:ClientLibraryFolder"` inside of `.content.xml`
   * files.
   */
  val attributeInClientLibraryFolder = PlatformPatterns.psiElement(XML_NAME)
      .inside(xmlTag().withName("jcr:root").with(xmlTagWithAttribute(
          "jcr:primaryType", "cq:ClientLibraryFolder")))
      .inFile(xmlFile().withName(".content.xml"))

}

/**
 * Create [PatternCondition] that will match a tag that contains an attribute
 * with given name and value.
 *
 * @param name the name of attribute
 * @param value the value of attribute
 */
fun xmlTagWithAttribute(name: String, value: String) : PatternCondition<XmlTag?> =
    object: PatternCondition<XmlTag?> ("Tag with $name=$value") {
      override fun accepts(tag: XmlTag, context: ProcessingContext?): Boolean =
          tag.hasAttribute {
            it.name == name && it.value == value
          }
    }








