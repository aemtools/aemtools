package com.aemtools.completion.small.patterns

import com.aemtools.common.constant.const.xml.CQ_COMPONENT
import com.aemtools.common.patterns.HostCondition
import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlTokenType

object CqComponentPatterns {

  /**
   * Matches `jcr:root` tag with `cq:ClientLibraryFolder` `jcr:primaryType`.
   */
  val cqComponentRootTag = XmlPatterns.xmlTag().withName("jcr:root").with(xmlTagWithAttribute(
      "jcr:primaryType", CQ_COMPONENT))

  /**
   * Matches an attribute of `jcr:root` tag with
   * `jcr:primaryType="cq:Component"` inside of `.content.xml`
   * files.
   */
  val attributeInCqComponent = psiElement(XmlTokenType.XML_NAME)
      .inside(cqComponentRootTag)
      .inFile(JcrPatterns.contentXmlFile)

  /**
   * Matcher for jcr value of `sling:resourceSuperType` attribute.
   */
  val cqComponentSlingResourceSuperType = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition("sling:resourceSuperType value") { _, host, _ ->
        psiElement()
            .inside(XmlPatterns.xmlAttribute().withName("sling:resourceSuperType"))
            .inFile(JcrPatterns.contentXmlFile)
            .accepts(host)
      })

  /**
   * Matcher for jcr value of `componentGroup` attribute.
   */
  val cqComponentComponentGroup = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition("componentGroup value") { _, host, _ ->
        psiElement()
            .inside(XmlPatterns.xmlAttribute().withName("componentGroup"))
            .inFile(JcrPatterns.contentXmlFile)
            .accepts(host)
      })

  /**
   * Matcher for jcr value of `cq:isContainer` attribute.
   */
  val cqComponentIsContainer = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition("cq:isContainer value") { _, host, _ ->
        psiElement()
            .inside(XmlPatterns.xmlAttribute().withName("cq:isContainer"))
            .inFile(JcrPatterns.contentXmlFile)
            .accepts(host)
      })

  /**
   * Matcher for jcr value of `cq:noDecoration` attribute.
   */
  val cqComponentNoDecoration = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition("cq:noDecoration value") { _, host, _ ->
        psiElement()
            .inside(XmlPatterns.xmlAttribute().withName("cq:noDecoration"))
            .inFile(JcrPatterns.contentXmlFile)
            .accepts(host)
      })

 /* *//**
   * Matcher for jcr value of `dialogPath` attribute.
   *//*
  val cqComponentDialogPath = psiElement(JpTypes.VALUE_TOKEN)
      .with(HostCondition("dialogPath value") { _, host, _ ->
        psiElement()
            .inside(XmlPatterns.xmlAttribute().withName("dialogPath"))
            .inFile(JcrPatterns.contentXmlFile)
            .accepts(host)
      })*/
}
