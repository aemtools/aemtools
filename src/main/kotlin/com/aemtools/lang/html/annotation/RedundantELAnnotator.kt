package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.hasChild
import com.aemtools.completion.util.isInsideOF
import com.aemtools.constant.const.htl.DATA_SLY_INCLUDE
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.constant.const.message.annotator.SIMPLIFY_EXPRESSION
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * @author Dmytro_Troynikov
 */
class RedundantELAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is HtlHtlEl
                && element.isDumbStringLiteralEl()) {
            if (element.isInsideOF(DATA_SLY_USE)) {
                holder.createWarningAnnotation(element, SIMPLIFY_EXPRESSION)
                        .registerFix(ELSimplifyAction(element))
            }
            if (element.isInsideOF(DATA_SLY_INCLUDE)) {
                holder.createWarningAnnotation(element, SIMPLIFY_EXPRESSION)
                        .registerFix(ELSimplifyAction(element))
            }
        }
    }

    /**
     * Check if current current [HtlHtlEl] is a "Dumb String Literal", which mean
     * that the expression doesn't contain anything except the string literal, e.g.:
     * ```
     *   ${'static string'}
     * ```
     */
    fun HtlHtlEl.isDumbStringLiteralEl() : Boolean =
            this.hasChild(HtlStringLiteral::class.java)
        && !this.hasChild(com.aemtools.lang.htl.psi.HtlPropertyAccess::class.java)
        && !this.hasChild(com.aemtools.lang.htl.psi.HtlContextExpression::class.java)

    class ELSimplifyAction(val element: HtlHtlEl) : IntentionAction {
        override fun startInWriteAction(): Boolean = true

        override fun getFamilyName(): String = "HTL"

        override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?):
                Boolean = true

        override fun getText(): String = "Simplify expression"

        override fun invoke(project: Project, editor: Editor?, file: PsiFile) {
            val newValue = element.findChildrenByType(HtlStringLiteralMixin::class.java)
                    .firstOrNull()?.name ?: return
            val (start, end) = element.textRange.startOffset to element.textRange.endOffset

            val document = PsiDocumentManager.getInstance(project).getDocument(file)
                    ?: return
            document.replaceString(start, end, newValue)
            PsiDocumentManager.getInstance(project).commitDocument(document)

        }
    }
}