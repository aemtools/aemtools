package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.findParentByType
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
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
            holder.createWarningAnnotation(element, "The data-sly-unwrap attribute is redundant in sly tag.")
                    .registerFix(RemoveRedundantDataSlyUnwrapAction())
        }
    }

    class RemoveRedundantDataSlyUnwrapAction : IntentionAction {
        override fun getFamilyName(): String = "HTL Intentions"

        override fun startInWriteAction(): Boolean = true

        override fun getText(): String = "Remove attribute."

        override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

        override fun invoke(project: Project, editor: Editor, file: PsiFile) {
            val currentElement = file.findElementAt(editor.caretModel.offset)
                    ?: return
            val document = PsiDocumentManager.getInstance(project).getDocument(file)
                    ?: return

            val (start, end) = currentElement.textRange.startOffset to currentElement.textRange.endOffset
            document.replaceString(start, end, "")
        }
    }

}
