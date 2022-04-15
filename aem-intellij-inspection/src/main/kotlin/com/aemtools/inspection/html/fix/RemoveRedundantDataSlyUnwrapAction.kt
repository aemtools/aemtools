package com.aemtools.inspection.html.fix

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Primshyts
 */
class RemoveRedundantDataSlyUnwrapAction(
    val pointer: SmartPsiElementPointer<XmlAttribute>) : BaseHtlIntentionAction(
    { "Remove attribute." }
) {
  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return
    val psiDocumentManager = PsiDocumentManager.getInstance(project)
    val document = psiDocumentManager.getDocument(file)
        ?: return

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset
    document.replaceString(start, end, "")
    psiDocumentManager.commitDocument(document)
  }
}
