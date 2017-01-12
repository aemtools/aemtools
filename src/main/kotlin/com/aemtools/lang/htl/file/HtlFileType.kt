package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.highlight.HtlHighlighter
import com.aemtools.lang.htl.highlight.HtlTemplateHighlighter
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import java.nio.charset.Charset
import javax.swing.Icon

/**
* @author Dmytro Troynikov
*/
object HtlFileType : LanguageFileType(HtlLanguage), TemplateLanguageFileType {
    init {
        FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, object : EditorHighlighterProvider{
            override fun getEditorHighlighter(project: Project?,
                                              fileType: FileType,
                                              virtualFile: VirtualFile?,
                                              colors: EditorColorsScheme): EditorHighlighter {
                return HtlTemplateHighlighter(project, virtualFile, colors)
            }
        })
    }

    override fun getIcon(): Icon?
            = AllIcons.FileTypes.Html

    override fun getName(): String
            = "Htl"

    override fun getDefaultExtension(): String
            = "html"

    override fun getDescription(): String
            = "Htl files"

    override fun extractCharsetFromFileContent(project: Project?, file: VirtualFile?, content: CharSequence): Charset? {
        val associatedFileType = getAssociatedFileType(file, project) ?: return null

        return CharsetUtil.extractCharsetFromFileContent(project, file, associatedFileType, content)
    }

    private fun getAssociatedFileType(file: VirtualFile?, project: Project?): LanguageFileType? {
        if (project == null) {
            return null
        }
        val language = TemplateDataLanguageMappings.getInstance(project).getMapping(file)

        var associatedFileType: LanguageFileType? = null
        if (language != null) {
            associatedFileType = language.associatedFileType
        }

        if (language == null || associatedFileType == null) {
            associatedFileType = HtlLanguage.getDefaultTemplateLang()
        }
        return associatedFileType
    }
}