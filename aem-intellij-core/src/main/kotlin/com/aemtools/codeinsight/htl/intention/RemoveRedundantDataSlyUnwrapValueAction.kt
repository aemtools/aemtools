package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Kostiantyn Diachenko
 */
class RemoveRedundantDataSlyUnwrapValueAction(
    val pointer: SmartPsiElementPointer<XmlAttribute>) : BaseHtlIntentionAction(
    { "Remove data-sly-unwrap attribute value" }
) {
  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return
    val psiDocumentManager = PsiDocumentManager.getInstance(project)
    val document = psiDocumentManager.getDocument(file)
        ?: return

    val valueTextRange = element.valueElement?.textRange ?: return
    val (start, end) = valueTextRange.startOffset to valueTextRange.endOffset
    document.replaceString(start - 1, end, "")
    psiDocumentManager.commitDocument(document)
  }
}
