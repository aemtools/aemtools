package com.aemtools.inspection.fix

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class RemoveRedundantDataSlyUnwrapAction(
    val pointer: SmartPsiElementPointer<XmlAttribute>) : BaseHtlIntentionAction(
    { "Remove attribute." }
) {
  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return
    val document = PsiDocumentManager.getInstance(project).getDocument(file)
        ?: return

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset
    document.replaceString(start, end, "")
    PsiDocumentManager.getInstance(project).commitDocument(document)
  }
}
