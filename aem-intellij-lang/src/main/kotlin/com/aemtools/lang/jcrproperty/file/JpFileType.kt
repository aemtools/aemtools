package com.aemtools.lang.jcrproperty.file

import com.aemtools.lang.jcrproperty.JcrPropertyLanguage
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FakeFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

/**
 * @author Dmytro Primshyts
 */
object JpFileType : LanguageFileType(JcrPropertyLanguage) {
  override fun getDescription(): String {
    return ""
  }

  override fun getDefaultExtension(): String {
    return "xml"
  }

  override fun getName(): String {
    return "JcrProperty"
  }

  override fun getIcon(): Icon = AllIcons.FileTypes.Properties

}
