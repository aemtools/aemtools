package com.aemtools.lang.htl

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.psi.templateLanguages.TemplateLanguage

/**
 * @author Dmytro_Troynikov
 */
object HtlLanguage : Language("Htl"), TemplateLanguage {

  /**
   * Get default template language file type.
   *
   * @return default template file type
   */
  fun getDefaultTemplateLang(): LanguageFileType = StdFileTypes.HTML

}
