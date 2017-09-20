package com.aemtools.lang.clientlib.file

import com.aemtools.lang.clientlib.CdLanguage
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * @author Dmytro_Troynikov
 */
object CdFileType : LanguageFileType(CdLanguage) {

  override fun getIcon(): Icon = AllIcons.FileTypes.Text

  override fun getName() = "Clientlib definition"

  override fun getDefaultExtension(): String = "cd"

  override fun getDescription(): String = "Clientlib definition"

}
