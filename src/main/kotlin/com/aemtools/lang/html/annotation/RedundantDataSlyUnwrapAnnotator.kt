package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.findParentByType
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.lang.html.fix.RemoveRedundantDataSlyUnwrapAction
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro Troynikov
 */
class RedundantDataSlyUnwrapAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is XmlAttribute
                && element.text == DATA_SLY_UNWRAP
                && element.findParentByType(XmlTag::class.java)?.name?.equals("sly", true) ?: false) {
            holder.createWarningAnnotation(element, REDUNDANT_DATA_SLY_UNWRAP_MESSAGE)
                    .registerFix(RemoveRedundantDataSlyUnwrapAction())
        }
    }

    companion object {
        val REDUNDANT_DATA_SLY_UNWRAP_MESSAGE = "The data-sly-unwrap attribute is redundant in sly tag."
    }

}
