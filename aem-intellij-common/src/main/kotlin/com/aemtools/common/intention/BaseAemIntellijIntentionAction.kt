package com.aemtools.common.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Base class quick fix action for AEM Intellij Tools.
 *
 * @property text Function that should construct message for [getText].
 * @property family String used as family name.
 * @property startInWriteAction Flag that denotes whether or not the action should
 * be started in write action [startInWriteAction].
 * @property isAvailable Function that should determine whether or current action
 * available in given context. (default: will return `true` for any context)
 * @author Dmytro Primshyts
 */
abstract class BaseAemIntellijIntentionAction(
    private val text: () -> String,
    private val family: String,
    private val startInWriteAction: Boolean = true,
    private val isAvailable: (project: Project, editor: Editor?, file: PsiFile?) -> Boolean = { _, _, _ -> true }
) : IntentionAction, LocalQuickFix {

  override fun applyFix(project: Project, descriptor: ProblemDescriptor)
      = invoke(project, null, null)

  override fun getFamilyName(): String = family
  override fun startInWriteAction(): Boolean = startInWriteAction

  override fun getText(): String = text.invoke()
  override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean
      = isAvailable.invoke(project, editor, file)

}
