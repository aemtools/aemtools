package com.aemtools.action

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.lang.htl.icons.HtlIcons
import com.aemtools.service.detection.HtlDetectionService
import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.psiManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.ui.GuiUtils
import com.intellij.util.FileContentUtil.reparseOpenedFiles

/**
 * Action that allows to mark/unmark directory as Htl Root.
 *
 * `Mark As -> HTL Root`
 * `Mark As -> Unmark as HTL Root`
 *
 * @author Dmytro Troynikov
 */
class MarkAsHtlRootDirectoryAction : DumbAwareAction() {

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
                ?.firstOrNull()
        val project = e.project
        if (file == null
                || project == null
                || !file.isDirectory
                && e.place != ActionPlaces.PROJECT_VIEW_POPUP
                || !HtlDetectionService.mayBeMarked(file.path, project)) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        if (HtlDetectionService.isHtlRootDirectory(file.path, project)) {
            e.presentation.text = "Unmark as HTL Root"
            return
        }

        e.presentation.icon = HtlIcons.HTL_ROOT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
                ?.firstOrNull() ?: return
        val project = e.project
        if (!e.presentation.isEnabledAndVisible || project == null) {
            return
        }

        val path = file.path

        val htlRootDirectories = HtlRootDirectories.getInstance(project)
                ?: return

        if (e.presentation.text == "HTL Root") {
            htlRootDirectories.addRoot(path)
        } else {
            htlRootDirectories.removeRoot(path)
        }

        // attempt to flush cached files
        e.project?.psiManager()?.apply {
            reparseOpenedFiles()

            GuiUtils.invokeLaterIfNeeded(Runnable {
                dropPsiCaches()
            },
                    ModalityState.defaultModalityState(),
                    project.disposed)

            HtlTemplateIndex.rebuildIndex()
        }
    }

}
