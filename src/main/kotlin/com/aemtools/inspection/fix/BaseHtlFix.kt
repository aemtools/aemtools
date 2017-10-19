package com.aemtools.inspection.fix

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Base class for htl quick fix actions.
 *
 * @property text Function that should construct message for [getText].
 * @property family String used as family name. (default: "HTL Intentions")
 * @property startInWriteAction Flag that denotes whether or not the action should
 * be started in write action [startInWriteAction].
 * @property isAvailable Function that should determine whether or current action
 * available in given context. (default: will return `true` for any context)
 *
 * @author Dmytro Troynikov
 */
abstract class BaseHtlFix(
    val text: () -> String,
    val family: String = "HTL Intentions",
    val startInWriteAction: Boolean = true,
    val isAvailable: (project: Project, editor: Editor?, file: PsiFile?) -> Boolean = { _, _, _ -> true }
) : IntentionAction {

  override fun getFamilyName(): String = family
  override fun startInWriteAction(): Boolean = startInWriteAction

  override fun getText(): String = text.invoke()
  override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean
      = isAvailable.invoke(project, editor, file)

}
