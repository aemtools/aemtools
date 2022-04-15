package com.aemtools.completion.small.patterns

import com.aemtools.common.patterns.HostCondition
import com.aemtools.common.patterns.IWithJcrPatterns
import com.aemtools.completion.small.patterns.JcrPatterns.editConfigFile
import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTokenType

/**
 * @author Dmytro Primshyts
 */
object EditConfigPatterns : IWithJcrPatterns {

  /**
   * Matches an attribute name under `jcr:root` tag in [editConfigFile].
   */
  val attributeUnderJcrRoot = psiElement(XmlTokenType.XML_NAME)
      .inside(jcrRootTag().inFile(editConfigFile))

  /**
   * Matches `jcr:primaryType` inside of [editConfigFile].
   */
  val primaryTypeInEditConfig: PsiElementPattern.Capture<PsiElement> = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition(
          "jcr:primaryType in edit config root tag",
          { _, host, _ ->
            psiElement().inside(jcrPrimaryTypeAttribute())
                .inFile(editConfigFile)
                .accepts(host)
          }
      ))

  /**
   * Matches `cq:dialogMode` value inside of [editConfigFile].
   */
  val cqDialogModeValue = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition(
          "cq:dialogMode value",
          { _, host, _ ->
            psiElement().inside(cqDialogModeAttribute())
                .inFile(editConfigFile)
                .accepts(host)
          }
      ))

  /**
   * Matches `cq:inherit` value inside of [editConfigFile].
   */
  val cqInheritValue = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition(
          "cq:inherit value",
          { _, host, _ ->
            psiElement().inside(cqInheritAttribute())
                .inFile(editConfigFile)
                .accepts(host)
          }
      ))

  /**
   * Matches `cq:layout` value inside of [editConfigFile].
   */
  val cqLayoutValue = psiElement(JpTypes.VALUE_TOKEN)
    .with(HostCondition(
        "cq:layout value",
        {_,host,_->
          psiElement().inside(cqLayoutAttribute())
              .inFile(editConfigFile)
              .accepts(host)
        }
    ))

  /**
   * Matches value of `cq:actions`.
   */
  val cqActionsValue = psiElement(JpTypes.ARRAY_VALUE_TOKEN)
      .with(HostCondition("cq:actions value",
          { _, host, _ ->
            psiElement()
                .inside(XmlPatterns.xmlAttribute()
                    .withName("cq:actions"))
                .inFile(editConfigFile)
                .accepts(host)
          }))

}
