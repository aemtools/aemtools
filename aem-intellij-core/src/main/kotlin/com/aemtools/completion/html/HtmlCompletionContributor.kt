package com.aemtools.completion.html

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.html.provider.*
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseNoEl
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.aInHtlFile
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.attributeInHtlFile
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.valueOfXLinkChecker

/**
 * Completion contributor for HTML related completions.
 *
 * @author Dmytro_Troynikov
 */
class HtmlCompletionContributor : BaseCompletionContributor({

  basic(dataSlyUseNoEl, HtmlDataSlyUseCompletionProvider)

  smart(dataSlyUseNoEl, HtmlDataSlyUseCompletionProvider)

  basic(attributeInHtlFile, HtmlAttributeCompletionProvider)

  basic(aInHtlFile, HtmlHrefLinkCheckerCompletionProvider)

  basic(valueOfXLinkChecker, HtmlLinkCheckerValueCompletionProvider)

  basic(dataSlyIncludeNoEl, HtmlDataSlyIncludeCompletionProvider)
})

