package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.highlight.HtlTemplateHighlighter
import com.aemtools.lang.htl.icons.HtlIcons.HTL_FILE_ICON
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

    override fun getIcon(): Icon = HTL_FILE_ICON

    override fun getName() = "HTL"

    override fun getDefaultExtension() = "htl"

    override fun getDescription() = "HTL File"

}