package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.highlight.HtlTemplateHighlighter
import com.aemtools.lang.htl.icons.HtlIcons.HTL_FILE_ICON
import com.aemtools.service.detection.HtlDetectionService
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
object HtlFileType : LanguageFileType(HtlLanguage), TemplateLanguageFileType, FileTypeIdentifiableByVirtualFile {

  init {
    FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this)
    { project, _, virtualFile, colors -> HtlTemplateHighlighter(project, virtualFile, colors) }
  }

  override fun isMyFileType(file: VirtualFile): Boolean {
    val project = ProjectLocator.getInstance().guessProjectForFile(file)

    if (file.isDirectory
        || project == null
        || file.extension != "html") {
      return false
    }

    val path = file.path
    return HtlDetectionService.isHtlFile(path, project)
  }

  override fun getIcon(): Icon = HTL_FILE_ICON

  override fun getName() = "HTL"

  override fun getDefaultExtension() = "htl"

  override fun getDescription() = "HTL File"

}
