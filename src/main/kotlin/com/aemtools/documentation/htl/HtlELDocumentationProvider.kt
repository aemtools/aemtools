package com.aemtools.documentation.htl

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement

/**
 *
 * @author Dmytro Troynikov.
 */
open class HtlELDocumentationProvider : AbstractDocumentationProvider() {

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