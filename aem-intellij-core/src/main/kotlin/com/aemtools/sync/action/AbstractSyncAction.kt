package com.aemtools.sync.action

import com.aemtools.sync.util.isUnderAEMRoot
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * @author Dmytro Liakhov
 */
abstract class AbstractSyncAction : AnAction() {

  override fun update(event: AnActionEvent) {
    val file = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.firstOrNull()

    if (file != null && file.isUnderAEMRoot()) {
      event.presentation.isEnabledAndVisible = true
    }
  }

}