package com.aemtools.documentation.htl.el

import com.aemtools.documentation.BaseDocumentationProvider
import com.intellij.psi.PsiElement

/**
 *
 * @author Dmytro Troynikov.
 */
open class HtlELDocumentationProvider : BaseDocumentationProvider() {

    override fun acceptGenerateDoc(element: PsiElement): Boolean {
        return false
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
//        val result = super.generateDoc(element, originalElement)
//        return result as String
        println("Hello HtlEl documentation provider!")

        return super.generateDoc(element, originalElement)
    }


    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
//        val result = super.getQuickNavigateInfo(element, originalElement)
//        return result as String
        return super.generateDoc(element, originalElement)
    }

}