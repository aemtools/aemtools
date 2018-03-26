package com.aemtools.completion.html

import com.aemtools.completion.html.provider.HtmlAttributeCompletionProvider
import com.aemtools.completion.html.provider.HtmlDataSlyIncludeCompletionProvider
import com.aemtools.completion.html.provider.HtmlDataSlyUseCompletionProvider
import com.aemtools.completion.html.provider.HtmlHrefLinkCheckerCompletionProvider
import com.aemtools.completion.html.provider.HtmlLinkCheckerValueCompletionProvider
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseNoEl
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.aInHtlFile
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.attributeInHtlFile
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.valueOfXLinkChecker
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

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
      attributeInHtlFile,
      HtmlAttributeCompletionProvider)

  extend(CompletionType.BASIC,
      aInHtlFile,
      HtmlHrefLinkCheckerCompletionProvider
  )

  extend(CompletionType.BASIC,
      valueOfXLinkChecker,
      HtmlLinkCheckerValueCompletionProvider
  )

  extend(CompletionType.BASIC,
      dataSlyIncludeNoEl,
      HtmlDataSlyIncludeCompletionProvider)
}
}
