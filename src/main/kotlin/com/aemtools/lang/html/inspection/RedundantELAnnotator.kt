package com.aemtools.lang.html.inspection

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.hasChild
import com.aemtools.completion.util.isInsideOF
import com.aemtools.constant.const.htl.DATA_SLY_INCLUDE
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.constant.const.message.annotator.SIMPLIFY_EXPRESSION
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.testFramework.MockProblemDescriptor

/**
 * @author Dmytro_Troynikov
 */
class RedundantELAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is HtlHtlEl && accept(element)) {
            holder.createWarningAnnotation(element, SIMPLIFY_EXPRESSION)
                    .registerFix(ELSimplifyAction(element),
                            element.textRange,
                            HighlightDisplayKey.find("Annotator"),
                            MockProblemDescriptor(element,
                                    SIMPLIFY_EXPRESSION,
                                    ProblemHighlightType.WEAK_WARNING,
                                    ELSimplifyAction(element)))
        }
    }

    private fun accept(element: PsiElement): Boolean = (element is HtlHtlEl
            && element.isDumbStringLiteralEl()
            && (element.isInsideOF(DATA_SLY_USE) || element.isInsideOF(DATA_SLY_INCLUDE)))

    /**
     * Check if current current [HtlHtlEl] is a "Dumb String Literal", which mean
     * that the expression doesn't contain anything except the string literal, e.g.:
     * ```
     *   ${'static string'}
     * ```
     */
    fun HtlHtlEl.isDumbStringLiteralEl(): Boolean =
            this.hasChild(HtlStringLiteral::class.java)
                    && !this.hasChild(com.aemtools.lang.htl.psi.HtlPropertyAccess::class.java)
                    && !this.hasChild(com.aemtools.lang.htl.psi.HtlContextExpression::class.java)

    /**
     * Removes the redundant el
     */
    class ELSimplifyAction(val element: HtlHtlEl) : LocalQuickFixOnPsiElement(element) {
        override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
            val newValue = element.findChildrenByType(HtlStringLiteralMixin::class.java)
                    .firstOrNull()?.name ?: return
            val (start, end) = element.textRange.startOffset to element.textRange.endOffset

            val document = PsiDocumentManager.getInstance(project).getDocument(file)
                    ?: return
            document.replaceString(start, end, newValue)
        }

        override fun getFamilyName(): String = "HTL Intentions"

        override fun getText(): String = "Simplify expression"
    }
}