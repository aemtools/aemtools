package com.aemtools.completion.small.patterns

import com.aemtools.common.patterns.HostCondition
import com.aemtools.common.patterns.IWithJcrPatterns
import com.aemtools.completion.small.patterns.JcrPatterns.editConfigFile
import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Primshyts
 */
object EditConfigPatterns : IWithJcrPatterns {

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
   * Matches value of `cq:actions`.
   */
  val cqActionsValue = psiElement(JpTypes.ARRAY_VALUE_TOKEN)
      .with(HostCondition("cq:actions value",
          { element, host, context ->
            psiElement()
                .inside(XmlPatterns.xmlAttribute()
                    .withName("cq:actions"))
                .inFile(editConfigFile)
                .accepts(host)
          }))

}
