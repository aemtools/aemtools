package com.aemtools.lang.clientlib

import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutor

/**
 * @author Dmytro_Troynikov
 */
class CdLanguageSubstitutor : LanguageSubstitutor() {

  override fun getLanguage(file: VirtualFile, project: Project): Language? {
    return if (file.name in listOf("js.txt", "css.txt")) {
      CdLanguage
    } else {
      null
    }
  }
}
