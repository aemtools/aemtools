package com.aemtools.lang.jcrproperty.file

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.ex.FakeFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

/**
 * @author Dmytro Primshyts
 */
object JpFileType : FakeFileType() {
  override fun getDescription(): String {
    return ""
  }

  override fun getName(): String {
    return "JcrProperty"
  }

  override fun isMyFileType(file: VirtualFile): Boolean = false

  override fun getIcon(): Icon = AllIcons.FileTypes.Properties

}
