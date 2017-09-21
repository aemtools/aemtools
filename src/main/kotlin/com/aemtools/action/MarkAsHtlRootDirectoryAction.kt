package com.aemtools.action

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.lang.htl.icons.HtlIcons
import com.aemtools.service.detection.HtlDetectionService
import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.psiManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
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

  override fun update(event: AnActionEvent) {
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        ?.firstOrNull()
    val project = event.project
    if (file == null
        || project == null
        || shouldDisable(file, project, event)) {
      event.presentation.isEnabledAndVisible = false
      return
    }

    if (HtlDetectionService.isHtlRootDirectory(file.path, project)) {
      event.presentation.text = "Unmark as HTL Root"
      return
    }

    event.presentation.icon = HtlIcons.HTL_ROOT
  }

  override fun actionPerformed(event: AnActionEvent) {
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        ?.firstOrNull() ?: return
    val project = event.project
    if (!event.presentation.isEnabledAndVisible || project == null) {
      return
    }

    val path = file.path

    val htlRootDirectories = HtlRootDirectories.getInstance(project)
        ?: return

    if (shouldAdd(event)) {
      htlRootDirectories.addRoot(path)
    } else {
      htlRootDirectories.removeRoot(path)
    }

    // attempt to flush cached files
    event.project?.psiManager()?.apply {
      reparseOpenedFiles()

      HtlTemplateIndex.rebuildIndex()
    }
  }

  private fun shouldAdd(event: AnActionEvent) = event.presentation.text == "HTL Root"

  private fun shouldDisable(file: VirtualFile,
                            project: Project,
                            event: AnActionEvent): Boolean {
    return (!file.isDirectory
        && event.place != ActionPlaces.PROJECT_VIEW_POPUP
        || !HtlDetectionService.mayBeMarked(file.path, project))
  }

}
