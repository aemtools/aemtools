package com.aemtools.common.util

import com.intellij.lang.Language
import com.intellij.lang.StdLanguages
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.rename.RenamePsiElementProcessor

/**
 * Psi utility & extension methods
 * @author Dmytro Primshyts
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

/**
 * Convert current [PsiElement] into [SmartPsiElementPointer].
 *
 * @receiver [PsiElement] based item
 * @return [SmartPsiElementPointer] pointing to current [PsiElement]
 */
inline fun <reified T : PsiElement> T.toSmartPointer(): SmartPsiElementPointer<T>
    = SmartPointerManager.getInstance(project)
    .createSmartPsiElementPointer(this)
