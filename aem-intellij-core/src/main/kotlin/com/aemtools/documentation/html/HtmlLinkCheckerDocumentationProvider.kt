package com.aemtools.documentation.html

import com.aemtools.lang.html.psi.pattern.HtmlPatterns.valueOfXLinkChecker
import com.aemtools.lang.html.psi.pattern.HtmlPatterns.xLinkCheckerAttribute
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlToken

/**
 * @author Dmytro Primshyts
 */
class HtmlLinkCheckerDocumentationProvider : AbstractDocumentationProvider() {

  override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
    return when {
      xLinkCheckerAttribute.accepts(originalElement) ->
        "Link checker configuration"
      valueOfXLinkChecker.accepts(originalElement) -> {
        val attribute = originalElement as? XmlToken
            ?: return super.generateDoc(element, originalElement)

        val value = attribute.text

        when (value) {
          "skip" -> "This link will be ignored by Link checker"
          "valid" -> "Link checker will check this link and mark as valid"
          else -> super.generateDoc(element, originalElement)
        }
      }

      else -> super.generateDoc(element, originalElement)
    }
  }

}
