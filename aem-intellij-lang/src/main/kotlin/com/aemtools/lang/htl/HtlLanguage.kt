package com.aemtools.lang.htl

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.psi.templateLanguages.TemplateLanguage

/**
 * @author Dmytro Primshyts
 */
object HtlLanguage : Language("Htl"), TemplateLanguage {

  /**
   * Get default template language file type.
   *
   * @return default template file type
   */
  fun getDefaultTemplateLang(): LanguageFileType = HtmlFileType.INSTANCE

}
