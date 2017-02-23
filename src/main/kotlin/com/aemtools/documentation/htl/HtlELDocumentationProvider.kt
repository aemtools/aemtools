package com.aemtools.documentation.htl

import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement

/**
 *
 * @author Dmytro Troynikov.
 */
open class HtlELDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (originalElement != null && optionName.accepts(originalElement)) {
            val text = originalElement.text
            // todo generate doc
        }

        return super.generateDoc(element, originalElement)
    }

}