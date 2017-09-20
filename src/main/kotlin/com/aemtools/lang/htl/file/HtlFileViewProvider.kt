package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.HtlLanguage.getDefaultTemplateLang
import com.aemtools.lang.htl.psi.HtlTypes.HEL
import com.aemtools.lang.htl.psi.HtlTypes.OUTER_LANGUAGE
import com.intellij.lang.Language
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.util.containers.ContainerUtil
import gnu.trove.THashSet
import java.util.concurrent.ConcurrentMap

/**
 * @author Dmytro Troynikov
 */
class HtlFileViewProvider @JvmOverloads constructor(
    manager: PsiManager,
    virtualFile: VirtualFile,
    physical: Boolean,
    val myBaseLanguage: Language,
    val myTemplateLanguage: Language = getTemplateDataLanguage(manager, virtualFile))
  : MultiplePsiFilesPerDocumentFileViewProvider(manager, virtualFile, physical),
    ConfigurableTemplateLanguageFileViewProvider {

  companion object {
    private val TEMPLATE_DATA_TO_LANG: ConcurrentMap<String, TemplateDataElementType>
        = ContainerUtil.newConcurrentMap()

    private fun getTemplateDataElementType(lang: Language): TemplateDataElementType {
      var result = TEMPLATE_DATA_TO_LANG.get(lang.id)

      if (result != null) {
        return result
      }

      val created = TemplateDataElementType("SIGHTLY_DATA_TEMPLATE", lang, OUTER_LANGUAGE, HEL)

      return TEMPLATE_DATA_TO_LANG.putIfAbsent(lang.id, created) ?: created
    }
  }

  override fun getBaseLanguage(): Language = myBaseLanguage

  override fun getTemplateDataLanguage(): Language = myTemplateLanguage

  override fun supportsIncrementalReparse(rootLanguage: Language): Boolean = false

  override fun getLanguages(): Set<Language> {
    return THashSet<Language>(listOf(myBaseLanguage, myTemplateLanguage))
  }

  override fun cloneInner(virtualFile: VirtualFile): MultiplePsiFilesPerDocumentFileViewProvider {
    return HtlFileViewProvider(manager, virtualFile, false, myBaseLanguage, myTemplateLanguage)
  }

  override fun createFile(lang: Language): PsiFile? {
    val parserDefinition = getDefinition(lang) ?: return null

    return when {
      lang.`is`(templateDataLanguage) -> {
        val file: PsiFileImpl = parserDefinition.createFile(this) as PsiFileImpl
        file.contentElementType = getTemplateDataElementType(myBaseLanguage)
        file
      }
      lang.isKindOf(baseLanguage) -> {
        parserDefinition.createFile(this)
      }
      else -> null
    }
  }

  private fun getDefinition(lang: Language): ParserDefinition? {
    return if (lang.isKindOf(myBaseLanguage))
      LanguageParserDefinitions.INSTANCE.forLanguage(if (lang.`is`(baseLanguage)) lang else baseLanguage)
    else
      LanguageParserDefinitions.INSTANCE.forLanguage(lang)
  }

}

/**
 * Get template data language.
 *
 * @param manager the psi manager
 * @param virtualFile the virtual file
 *
 * @return the template language
 */
fun getTemplateDataLanguage(manager: PsiManager, virtualFile: VirtualFile): Language {

  val mappings = TemplateDataLanguageMappings.getInstance(manager.project)
  var dataLang: Language? = null

  if (mappings != null) {
    dataLang = mappings.getMapping(virtualFile)
  }

  if (dataLang == null) {
    dataLang = getDefaultTemplateLang().language
  }

  val substituteLanguage = LanguageSubstitutors.INSTANCE.substituteLanguage(dataLang, virtualFile, manager.project)

  if (TemplateDataLanguageMappings.getTemplateableLanguages().contains(substituteLanguage)) {
    dataLang = substituteLanguage
  }

  return dataLang

}
