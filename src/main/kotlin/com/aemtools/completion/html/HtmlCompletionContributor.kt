package com.aemtools.completion.html

import com.aemtools.completion.html.provider.HtmlAttributeCompletionProvider
import com.aemtools.completion.html.provider.HtmlDataSlyIncludeCompletionProvider
import com.aemtools.completion.html.provider.HtmlDataSlyUseCompletionProvider
import com.aemtools.lang.htl.psi.pattern.HtlFilePattern
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseNoEl
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.XmlPatterns.xmlAttribute
import com.intellij.psi.xml.XmlTokenType

/**
 * Completion contributor for HTML related completions.
 *
 * @author Dmytro_Troynikov
 */
class HtmlCompletionContributor : CompletionContributor() { init {
  extend(CompletionType.BASIC,
      dataSlyUseNoEl,
      HtmlDataSlyUseCompletionProvider)

  extend(CompletionType.SMART,
      dataSlyUseNoEl,
      HtmlDataSlyUseCompletionProvider)

  extend(CompletionType.BASIC,
      psiElement(XmlTokenType.XML_NAME)
          .inside(xmlAttribute())
          .inFile(PlatformPatterns.psiFile().with(HtlFilePattern)),
      HtmlAttributeCompletionProvider)

  extend(CompletionType.BASIC,
      dataSlyIncludeNoEl,
      HtmlDataSlyIncludeCompletionProvider)
}
}
