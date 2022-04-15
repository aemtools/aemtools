package com.aemtools.lang.clientlib.psi

import com.aemtools.lang.clientlib.CdLanguage
import com.aemtools.lang.clientlib.file.CdFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

/**
 * @author Dmytro Primshyts
 */
class CdPsiFile(fileViewProvider: FileViewProvider)
  : PsiFileBase(fileViewProvider, CdLanguage) {
  override fun getFileType() = CdFileType

  override fun toString() = "CdFile:$name"
}
