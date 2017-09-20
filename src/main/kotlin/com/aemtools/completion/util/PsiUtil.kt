package com.aemtools.completion.util

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.aemtools.util.psiManager
import com.intellij.lang.Language
import com.intellij.lang.StdLanguages
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.rename.RenamePsiElementProcessor

/**
 * Psi utility & extension methods
 * @author Dmytro_Troynikov
 */

/**
 * Get [PsiFile] for specified [Language].
 *
 * @param language the language
 *
 * @receiver [PsiFile]
 * @return the [PsiFile] for given [Language], *null* in case if no file was found
 */
fun PsiFile.getPsi(language: Language): PsiFile? = viewProvider.getPsi(language)

/**
 * Get [PsiFile] for [StdLanguages.HTML] language.
 *
 * @receiver [PsiFile]
 * @return psi file for html language, *null* if no such file available
 */
fun PsiFile.getHtmlFile(): PsiFile? = getPsi(StdLanguages.HTML)

/**
 * Get [XmlFile] from current [PsiFile].
 *
 * @receiver [PsiFile]
 * @return psi file for [StdLanguages.XML] language, *null* if no such file available
 */
fun PsiFile.getXmlFile(): XmlFile? = getPsi(StdLanguages.XML) as? XmlFile

/**
 * Get [HtlPsiFile] from current [PsiFile].
 *
 * @receiver [PsiFile]
 * @return psi file for [HtlLanguage] language, *null* if no such file available
 */
fun PsiFile.getHtlFile(): HtlPsiFile? = getPsi(HtlLanguage) as? HtlPsiFile

/**
 * Convert current [VirtualFile] to [PsiFile].
 *
 * @param project the project
 * @receiver [VirtualFile]
 * @see [com.intellij.psi.PsiManager.findFile]
 * @return the psi file, may be *null*
 */
fun VirtualFile.toPsiFile(project: Project): PsiFile? = project.psiManager().findFile(this)

/**
 * Convert current [VirtualFile] to [PsiDirectory].
 *
 * @param project the project
 * @receiver [VirtualFile]
 * @see [com.intellij.psi.PsiManager.findDirectory]
 * @return the psi directory]
 */
fun VirtualFile.toPsiDirectory(project: Project): PsiDirectory? = project.psiManager().findDirectory(this)

/**
 * Get resource type from current [VirtualFile].
 *
 * @receiver [VirtualFile]
 *
 * @return resource type of current virtual file
 */
fun VirtualFile.resourceType(): String? {
  var currentDir: VirtualFile? = this
  while (currentDir != null) {
    if (currentDir.findChild(".content.xml") != null) {
      return currentDir.path.normalizeToJcrRoot()
    }
    currentDir = currentDir.parent
  }
  return null
}

/**
 * Get [VirtualFile] instance that contain current [PsiElement].
 *
 * @receiver [PsiElement]
 * @see [PsiUtilCore.getVirtualFile]
 *
 * @return instance of virtual file
 */
fun PsiElement.virtualFile(): VirtualFile? =
    PsiUtilCore.getVirtualFile(this)

/**
 * Find incoming references (usages) for current element.
 *
 * @receiver [PsiElement]
 *
 * @return list of incoming references
 */
fun PsiElement.incomingReferences(): List<PsiReference> = runReadAction {
  RenamePsiElementProcessor.forElement(this).findReferences(this)
      .toList()
}
