package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.highlight.HtlTemplateHighlighter
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.TemplateLanguageFileType
import javax.swing.Icon

/**
* @author Dmytro Troynikov
*/
object HtlFileType : LanguageFileType(HtlLanguage), TemplateLanguageFileType {
    init {
        FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this)
        { project, _, virtualFile, colors -> HtlTemplateHighlighter(project, virtualFile, colors) }
    }

    override fun getIcon(): Icon = AllIcons.FileTypes.Json

    override fun getName() = "Htl"

    override fun getDefaultExtension() = "htl"

    override fun getDescription() = "HTL File"

}