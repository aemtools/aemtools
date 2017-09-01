package com.aemtools.action

import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.psiManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAwareAction

/**
 * @author Dmytro Troynikov
 */
class MarkAsHtlRootDirectoryAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
                ?.firstOrNull() ?: return

        val path = file.path

        HtlRootDirectories.getInstance()
                ?.let { htlRootDirectories ->
                    htlRootDirectories.addRoot(path)

                    // attempt to flush cached files
                    e.project?.psiManager()?.apply {
                        dropPsiCaches()
                        dropResolveCaches()
                    }
                }

    }

}
