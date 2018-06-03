package com.aemtools.completion.small.patterns

import com.aemtools.common.patterns.HostCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns

/**
 * @author Dmytro Primshyts
 */
object ClientlibraryFolderPatterns {
  /**
   * Matcher for jcr value of `categories` attribute.
   */
  val jcrArrayValueOfCategories = JcrPatterns.jcrArrayValue
      .with(HostCondition(
          "categoriess value",
          { _, host, _ ->
            PlatformPatterns.psiElement().inside(XmlPatterns.xmlAttribute().withName("categories"))
                .inside(JcrPatterns.clientLibraryRootTag)
                .accepts(host)
          }
      ))

  /**
   * Matcher for jcr value of `dependencies` attribute.
   */
  val jcrArrayValueOfDependencies = JcrPatterns.jcrArrayValue
      .with(HostCondition(
          "dependencies value",
          { _, host, _ ->
            PlatformPatterns.psiElement()
                .inside(XmlPatterns
                    .xmlAttribute()
                    .withName("dependencies"))
                .inside(JcrPatterns.clientLibraryRootTag)
                .inFile(JcrPatterns.contentXmlFile)
                .accepts(host)
          }
      ))

  /**
   * Matcher for jcr value of `embeds` attribute.
   */
  val jcrArrayValueOfEmbeds = JcrPatterns.jcrArrayValue
      .with(HostCondition(
          "embed value",
          { _, host, _ ->
            PlatformPatterns.psiElement()
                .inside(XmlPatterns
                    .xmlAttribute()
                    .withName("embed"))
                .inside(JcrPatterns.clientLibraryRootTag)
                .inFile(JcrPatterns.contentXmlFile)
                .accepts(host)
          }
      ))
}
