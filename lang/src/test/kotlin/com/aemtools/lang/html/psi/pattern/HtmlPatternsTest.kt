package com.aemtools.lang.html.psi.pattern

import com.aemtools.lang.html.psi.pattern.HtmlPatterns.aInHtlFile
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.attributeInHtlFile
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.valueOfXLinkChecker
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.xLinkCheckerAttribute
import com.aemtools.test.pattern.BasePatternsTest
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class HtmlPatternsTest : BasePatternsTest() {

  fun testAAttributeInHtlFile() = testHtmlPattern(
      aInHtlFile,
      "<a href=\"\" $CARET></a>",
      true
  )

  fun testAAttributeInHtlFileNegative() = testHtmlPattern(
      aInHtlFile,
      "<div $CARET></div>",
      false
  )

  fun testAttributeInHtlFile() = testHtmlPattern(
      attributeInHtlFile,
      "<div $CARET></div>",
      true
  )

  fun testValueOfXLinkChecker() = testHtmlPattern(
      valueOfXLinkChecker,
      "<a x-cq-linkchecker=\"$CARET\"></a>",
      true
  )

  fun testValueOfXLinkCheckerNegative() = testHtmlPattern(
      valueOfXLinkChecker,
      "<a href=\"$CARET\"></a>",
      false
  )

  fun testXLinkCheckerAttribute() = testHtmlPattern(
      xLinkCheckerAttribute,
      "<a ${CARET}x-cq-linkchecker=''",
      true,
      false
  )

  fun testXLinkCheckerAttributeNegative() = testHtmlPattern(
      xLinkCheckerAttribute,
      "<a ${CARET}not-linkchecker=''",
      false
  )

  private fun testHtmlPattern(
      pattern: ElementPattern<PsiElement>,
      text: String,
      result: Boolean,
      addCompletionPlaceholder: Boolean = true) =
      testPattern(
          pattern,
          text,
          result,
          addCompletionPlaceholder,
          { textToAdd -> addHtml("test.html", textToAdd) }
      )

}
