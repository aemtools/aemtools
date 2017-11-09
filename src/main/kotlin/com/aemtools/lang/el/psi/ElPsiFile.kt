package com.aemtools.lang.el.psi

import com.aemtools.lang.el.ElLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

/**
 * @author Dmytro Troynikov
 */
class ElPsiFile(fileViewProvider: FileViewProvider)
  : PsiFileBase(fileViewProvider, ElLanguage) {
  override fun getFileType(): FileType {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
