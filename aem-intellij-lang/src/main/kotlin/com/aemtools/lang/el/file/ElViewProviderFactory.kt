package com.aemtools.lang.el.file

import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.SingleRootFileViewProvider

/**
 * @author Dmytro Primshyts
 */
class ElViewProviderFactory : FileViewProviderFactory {
  override fun createFileViewProvider(
      file: VirtualFile,
      language: Language?,
      manager: PsiManager,
      eventSystemEnabled: Boolean): FileViewProvider {
    return SingleRootFileViewProvider(manager, file, eventSystemEnabled, HtlFileType)
  }
}
