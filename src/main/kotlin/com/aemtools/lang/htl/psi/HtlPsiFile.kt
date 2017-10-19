package com.aemtools.lang.htl.psi

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

/**
 * @author Dmytro Troynikov
 */
class HtlPsiFile(fileViewProvider: FileViewProvider)
  : PsiFileBase(fileViewProvider, HtlLanguage) {

  override fun getFileType() = HtlFileType

  override fun toString() = "HtlFile:$name"

}
