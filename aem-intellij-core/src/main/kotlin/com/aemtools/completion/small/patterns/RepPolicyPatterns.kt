package com.aemtools.completion.small.patterns

import com.aemtools.common.util.injectedLanguageManager
import com.aemtools.completion.small.patterns.JcrPatterns.aclRootTag
import com.aemtools.completion.small.patterns.JcrPatterns.repPolicyFile
import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.patterns.XmlPatterns.xmlTag
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTokenType.XML_NAME
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object RepPolicyPatterns {

  /**
   * Matches `allow` or `deny` subtag name
   */
  val tagUnderAllowOrDeny = psiElement()
      .afterLeaf("<")
      .inside(xmlTag().inside(aclRootTag))
      .inFile(repPolicyFile)

  /**
   * Matches tag - direct child of [aclRootTag].
   */
  val tagUnderAclRoot = psiElement(XML_NAME)
      .afterLeaf("<")
      .inside(aclRootTag)
      .inFile(repPolicyFile)

  /**
   * Matches attribute name in [aclRootTag] subtag.
   */
  val attributeNameUnderAcl = psiElement(XML_NAME)
      .inside(xmlTag())
      .inside(aclRootTag)
      .inFile(repPolicyFile)

  /**
   * Matches name of attribute inside `rep:restrictions` tag.
   */
  val repRestrictionAttributeName = psiElement(XML_NAME)
      .inside(xmlTag().withName("rep:restrictions"))
      .inside(aclRootTag)
      .inFile(repPolicyFile)

  /**
   * Matches value of `rep:privileges` in [aclRootTag] subtag.
   */
  val privilegesValue = psiElement(JpTypes.ARRAY_VALUE_TOKEN)
      .with(object : PatternCondition<PsiElement?>(
          "rep:privileges value"
      ) {
        override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
          val host = t.project.injectedLanguageManager().getInjectionHost(t)
              ?: return false

          return psiElement()
              .inside(XmlPatterns.xmlAttribute()
                  .withName("rep:privileges"))
              .inside(aclRootTag)
              .inFile(repPolicyFile)
              .accepts(host)
        }
      })

  /**
   * Matches `jcr:primaryType`'s value within [aclRootTag] subtag.
   */
  val primaryTypeInAcl: PsiElementPattern.Capture<PsiElement> = psiElement(JpTypes.VALUE_TOKEN)
      .with(object : PatternCondition<PsiElement?>("jcr:primaryType in aclRootTag") {
        override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
          val host = t.project.injectedLanguageManager().getInjectionHost(t)
              ?: return false

          return psiElement()
              .inside(XmlPatterns.xmlAttribute()
                  .withName("jcr:primaryType"))
              .inside(aclRootTag)
              .inFile(repPolicyFile)
              .accepts(host)
        }
      })

}
