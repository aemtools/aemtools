package com.aemtools.lang.clientlib.highlight

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Highlighter factory for [com.aemtools.lang.clientlib.CdLanguage].
 *
 * @author Dmytro_Troynikov
 */
class CdHighlighterFactory : SyntaxHighlighterFactory() {

  override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter
      = CdHighlighter()

}
