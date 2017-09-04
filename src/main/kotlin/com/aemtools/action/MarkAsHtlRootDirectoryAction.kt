package com.aemtools.action

import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.psiManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.ui.GuiUtils

/**
 * @author Dmytro Troynikov
 */
class MarkAsHtlRootDirectoryAction : DumbAwareAction() {

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
                ?.firstOrNull() ?: return

        if (!file.isDirectory && e.place != ActionPlaces.PROJECT_VIEW_POPUP) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        val htlRootDirectories = HtlRootDirectories.getInstance()

        if (htlRootDirectories != null && htlRootDirectories.directories.contains(file.path)) {
            e.presentation.text = "Unmark as HTL Root"
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
                ?.firstOrNull() ?: return

        if (!e.presentation.isEnabledAndVisible) {
            return
        }

        val path = file.path

        val htlRootDirectories = HtlRootDirectories.getInstance()
                ?: return

        if (e.presentation.text == "HTL Root") {
            htlRootDirectories.addRoot(path)
        } else {
            htlRootDirectories.removeRoot(path)
        }

        // attempt to flush cached files
        e.project?.psiManager()?.apply {
            GuiUtils.invokeLaterIfNeeded(Runnable {
                dropPsiCaches()
            },
                    ModalityState.defaultModalityState(),
                    project.disposed)
        }
    }


}
