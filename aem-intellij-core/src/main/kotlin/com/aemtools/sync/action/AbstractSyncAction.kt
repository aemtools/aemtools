package com.aemtools.sync.action

import com.aemtools.sync.settings.InstanceInfo
import com.aemtools.sync.util.isUnderAEMRoot
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

    if (file != null && file.isUnderAEMRoot()) {
      val project = event.project ?: return
      event.presentation.isEnabledAndVisible = isAEMSyncEnabled(project)
    }
  }

  private fun isAEMSyncEnabled(project: Project): Boolean {
    val instanceInfo = InstanceInfo.getInstance(project)
    return instanceInfo.enabled ?: false
  }

}
