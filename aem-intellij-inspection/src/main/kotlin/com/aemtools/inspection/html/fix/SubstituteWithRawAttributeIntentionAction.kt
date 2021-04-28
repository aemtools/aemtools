package com.aemtools.inspection.html.fix

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * Will `data-sly-attribute` with incorrect attribute by raw attribute e.g.:
 *
 * ```
 *  <div data-sly-attribute.style="${'...'}">
 *      -->
 *  <div style="${'...'}">
 * ```
 *
 * @author Dmytro Primshyts
 */
class SubstituteWithRawAttributeIntentionAction(
    private val pointer: SmartPsiElementPointer<XmlAttribute>,
    private val message: String
) : BaseHtlIntentionAction({ message }) {

  override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    val attribute = pointer.element ?: return

    attribute.name = attribute.name.substringAfterLast(".")
  }

}
