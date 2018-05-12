package com.aemtools.integration.sync.action

import com.aemtools.integration.sync.settings.AemToolsProjectConfiguration
import com.aemtools.integration.sync.util.isUnderJcrRoot
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project

/**
 * @author Dmytro Liakhov
 */
abstract class AbstractSyncAction : AnAction() {

  override fun update(event: AnActionEvent) {
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        ?.firstOrNull()

    if (file != null && file.isUnderJcrRoot()) {
      val project = event.project ?: return
      event.presentation.isEnabledAndVisible = isAEMSyncEnabled(project)
    }
  }

  private fun isAEMSyncEnabled(project: Project): Boolean {
    val instanceInfo = AemToolsProjectConfiguration.getInstance(project)
    return instanceInfo.isSyncEnabled
  }

}
