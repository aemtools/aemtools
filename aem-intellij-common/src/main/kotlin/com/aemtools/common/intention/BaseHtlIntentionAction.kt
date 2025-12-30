package com.aemtools.common.intention

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Base Htl intention action.
 *
 * @author Dmytro Primshyts
 */
abstract class BaseHtlIntentionAction(
    text: () -> String,
    family: String = "HTL Intentions",
    startInWriteAction: Boolean = true,
    isAvailable: (project: Project, editor: Editor?, file: PsiFile?) -> Boolean = { _, _, _ -> true }
) : BaseAemIntellijIntentionAction(
    text,
    family,
    startInWriteAction,
    isAvailable
)
