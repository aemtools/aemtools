package com.aemtools.inspection.fix

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class RemoveRedundantDataSlyUnwrapAction(val element: XmlAttribute) : BaseHtlFix(
    { "Remove attribute." }
) {
  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val document = PsiDocumentManager.getInstance(project).getDocument(file)
        ?: return

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset
    document.replaceString(start, end, "")
    PsiDocumentManager.getInstance(project).commitDocument(document)
  }
}
