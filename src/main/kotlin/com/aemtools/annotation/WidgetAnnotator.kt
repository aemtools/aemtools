package com.aemtools.annotation

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * @author Dmytro_Troynikov.
 */
class WidgetAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        throw UnsupportedOperationException()
    }

}