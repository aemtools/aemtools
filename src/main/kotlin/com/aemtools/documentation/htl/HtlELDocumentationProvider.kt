package com.aemtools.documentation.htl

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement

/**
 *
 * @author Dmytro Troynikov.
 */
open class HtlELDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        return super.generateDoc(element, originalElement)
    }

}