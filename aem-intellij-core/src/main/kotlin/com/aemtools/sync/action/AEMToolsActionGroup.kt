package com.aemtools.sync.action

import com.aemtools.sync.settings.InstanceInfo
import com.aemtools.sync.util.isUnderAEMRoot
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project

/**
 * @author Dmytro Liakhov
 */
class AEMToolsActionGroup : DefaultActionGroup() {

  override fun update(event: AnActionEvent?) {
    if (event == null) {
      return
    }
    val project = event.project ?: return
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.firstOrNull()
    val presentation = event.presentation

    presentation.isEnabledAndVisible = file != null && file.isUnderAEMRoot() && isAEMSyncEnabled(project)
  }

  private fun isAEMSyncEnabled(project: Project): Boolean {
    val instanceInfo = InstanceInfo.getInstance(project)
    return instanceInfo.enabled ?: false
  }

}
