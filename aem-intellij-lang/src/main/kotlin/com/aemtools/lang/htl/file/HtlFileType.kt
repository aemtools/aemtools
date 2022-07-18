package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.highlight.HtlTemplateHighlighter
import com.aemtools.lang.htl.icons.HtlIcons.HTL_FILE_ICON
import com.aemtools.lang.htl.service.HtlDetectionService
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.project.guessProjectForContentFile
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.impl.FakeVirtualFile
import javax.swing.Icon

/**
 * @author Dmytro Primshyts
 */
object HtlFileType
  : LanguageFileType(HtlLanguage),
    TemplateLanguageFileType,
    FileTypeIdentifiableByVirtualFile {

  init {
    FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this)
    { project, _, virtualFile, colors ->
      HtlTemplateHighlighter(project, virtualFile, colors)
    }
  }

  override fun isMyFileType(file: VirtualFile): Boolean {
    if (file.isDirectory
        || file.extension != "html"
        || file is FakeVirtualFile) {
      return false
    }

    val project = guessProjectForContentFile(file)
    val path = file.path

    return if (project == null) {
      HtlDetectionService.isHtlFile(path)
    } else {
      HtlDetectionService.isHtlFile(path, project)
    }
  }

  override fun getIcon(): Icon = HTL_FILE_ICON

  override fun getName() = "HTL"

  override fun getDefaultExtension() = "htl"

  override fun getDescription() = "HTL File"

}
