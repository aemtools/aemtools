package com.aemtools.lang.htl.highlight

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings

/**
 * @author Dmytro Troynikov
 */
class HtlTemplateHighlighter(val project: Project?,
                             val virtualFile: VirtualFile?,
                             scheme: EditorColorsScheme) :
    LayeredLexerEditorHighlighter(HtlHighlighter(), scheme) {
  init {
    var type: FileType = if (project == null || virtualFile == null) {
      StdFileTypes.PLAIN_TEXT
    } else {
      val language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile)
      if (language != null && language.associatedFileType != null) {
        language.associatedFileType as FileType
      } else {
        HtlLanguage.getDefaultTemplateLang()
      }
    }

    val outerHighlighter = SyntaxHighlighter.PROVIDER.create(type, project, virtualFile)

    registerLayer(HtlTypes.OUTER_LANGUAGE, LayerDescriptor(outerHighlighter, ""))
  }
}
