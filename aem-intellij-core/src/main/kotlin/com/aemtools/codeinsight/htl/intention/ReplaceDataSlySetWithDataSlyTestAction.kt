package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.constant.const.htl.DATA_SLY_SET
import com.aemtools.common.constant.const.htl.DATA_SLY_TEST
import com.aemtools.common.intention.BaseHtlIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Kostiantyn Diachenko
 */
class ReplaceDataSlySetWithDataSlyTestAction(
    private val pointer: SmartPsiElementPointer<XmlAttribute>,
) : BaseHtlIntentionAction(
    text = { "Replace with data-sly-test" }
) {
  override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    val attribute = pointer.element ?: return

    attribute.name = attribute.name.replaceFirst(DATA_SLY_SET, DATA_SLY_TEST)
  }
}
