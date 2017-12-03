package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage
import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager

/**
 * @author Dmytro Troynikov
 */
class HtlViewProviderFactory : FileViewProviderFactory {

  override fun createFileViewProvider(file: VirtualFile,
                                      language: Language?,
                                      manager: PsiManager,
                                      eventSystemEnabled: Boolean): FileViewProvider {
    if (language == null || !language.isKindOf(HtlLanguage)) {
      throw AssertionError("Htl language expected")
    }
    return HtlFileViewProvider(manager, file, eventSystemEnabled, language)
  }

}
