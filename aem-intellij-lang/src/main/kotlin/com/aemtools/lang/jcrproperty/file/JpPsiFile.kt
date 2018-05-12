package com.aemtools.lang.jcrproperty.file

import com.aemtools.lang.jcrproperty.JcrPropertyLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

/**
 * @author Dmytro Primshyts
 */
class JpPsiFile(fileViewProvider: FileViewProvider)
  : PsiFileBase(fileViewProvider, JcrPropertyLanguage) {

  override fun getFileType(): FileType = JpFileType

  override fun toString() = "JpFile:$name"

}
