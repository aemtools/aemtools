package com.aemtools.integration.sync.action

import com.aemtools.integration.sync.settings.AemToolsProjectConfiguration
import com.aemtools.integration.sync.util.isUnderJcrRoot
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project

/**
 * @author Dmytro Liakhov
 */
class AemToolsActionGroup : DefaultActionGroup() {

  override fun update(event: AnActionEvent?) {
    if (event == null) {
      return
    }
    val project = event.project ?: return
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        ?.firstOrNull()
    val presentation = event.presentation

    presentation.isEnabledAndVisible = file != null && file.isUnderJcrRoot() && isAEMSyncEnabled(project)
  }

  private fun isAEMSyncEnabled(project: Project): Boolean {
    val instanceInfo = AemToolsProjectConfiguration.getInstance(project)
    return instanceInfo.isSyncEnabled
  }

}
