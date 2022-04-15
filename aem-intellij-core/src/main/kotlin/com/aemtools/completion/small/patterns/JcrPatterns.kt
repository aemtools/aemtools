package com.aemtools.completion.small.patterns

import com.aemtools.common.patterns.IWithJcrPatterns
import com.aemtools.common.util.hasAttribute
import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlPatterns.xmlFile
import com.intellij.patterns.XmlPatterns.xmlTag
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlTokenType.XML_NAME
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object JcrPatterns : IWithJcrPatterns {

  /**
   * Matches `jcr:root` tag with `cq:ClientLibraryFolder` `jcr:primaryType`.
   */
  val clientLibraryRootTag = xmlTag().withName("jcr:root").with(xmlTagWithAttribute(
      "jcr:primaryType", "cq:ClientLibraryFolder"))

  /**
   * Matches `jcr:root` tag with `rep:ACL` `jcr:primaryType`.
   */
  val aclRootTag = jcrRootTag()
      .with(xmlTagWithAttribute(
          "jcr:primaryType", "rep:ACL"
      ))

  /**
   * Matches `.content.xml` file.
   */
  val contentXmlFile = xmlFile().withName(".content.xml")

  /**
   * Matches `_rep_policy.xml` file.
   */
  val repPolicyFile = xmlFile().withName("_rep_policy.xml")

  /**
   * Matches `_cq_editConfig.xml` file.
   */
  val editConfigFile = xmlFile().withName("_cq_editConfig.xml")

  /**
   * Matches an attribute of `jcr:root` tag with
   * `jcr:primaryType="cq:ClientLibraryFolder"` inside of `.content.xml`
   * files.
   */
  val attributeInClientLibraryFolder = PlatformPatterns.psiElement(XML_NAME)
      .inside(clientLibraryRootTag)
      .inFile(contentXmlFile)

  /**
   * Matches value of an item within jcr array.
   */
  val jcrArrayValue: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement()
      .inside(PlatformPatterns.psiElement(JpTypes.ARRAY))

  /**
   * Matches jcr type.
   */
  val jcrType: PsiElementPattern.Capture<PsiElement> = PlatformPatterns.psiElement()
      .afterLeaf("{")
      .beforeLeaf(PlatformPatterns.psiElement(JpTypes.RBRACE))

}

/**
 * Create [PatternCondition] that will match a tag that contains an attribute
 * with given name and value.
 *
 * @param name the name of attribute
 * @param value the value of attribute
 */
fun xmlTagWithAttribute(name: String, value: String): PatternCondition<XmlTag?> =
    object : PatternCondition<XmlTag?>("Tag with $name=$value") {
      override fun accepts(tag: XmlTag, context: ProcessingContext?): Boolean =
          tag.hasAttribute {
            it.name == name && it.value == value
          }
    }
