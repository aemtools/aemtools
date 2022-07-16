package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.aemtools.lang.settings.ui.AemProjectSettingsConfigurable
import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * @author Kostiantyn Diachenko
 */
class ChangeHtlVersionAction
  : BaseHtlIntentionAction(
    text = { "Change HTL version" },
    startInWriteAction = false
), LowPriorityAction {
  override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
    ShowSettingsUtil.getInstance().showSettingsDialog(project, AemProjectSettingsConfigurable::class.java)
  }
}
