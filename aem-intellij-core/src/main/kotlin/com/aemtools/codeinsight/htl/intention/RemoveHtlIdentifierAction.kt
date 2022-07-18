package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Kostiantyn Diachenko
 */
class RemoveHtlIdentifierAction(
    private val pointer: SmartPsiElementPointer<XmlAttribute>,
    private val message: String
) : BaseHtlIntentionAction(
    text = { message }
) {
  override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    val attribute = pointer.element ?: return

    attribute.name = attribute.name.substringBeforeLast(".")
  }
}
