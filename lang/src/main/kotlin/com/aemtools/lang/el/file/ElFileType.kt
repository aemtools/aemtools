package com.aemtools.lang.el.file

import com.aemtools.lang.el.ElLanguage
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
object ElFileType : LanguageFileType(ElLanguage) {

  override fun getIcon(): Icon = AllIcons.Icon

  override fun getName(): String = "Expression Language"

  override fun getDefaultExtension(): String = "el"

  override fun getDescription(): String = "EL Test File"

}
