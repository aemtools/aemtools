package com.aemtools.common.util

import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
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
 * Get [PsiFile] for [HTMLLanguage.INSTANCE] language.
 *
 * @receiver [PsiFile]
 * @return psi file for html language, *null* if no such file available
 */
fun PsiFile.getHtmlFile(): PsiFile? = getPsi(HTMLLanguage.INSTANCE)

/**
 * Get [XmlFile] from current [PsiFile].
 *
 * @receiver [PsiFile]
 * @return psi file for [XMLLanguage.INSTANCE] language, *null* if no such file available
 */
fun PsiFile.getXmlFile(): XmlFile? = getPsi(XMLLanguage.INSTANCE) as? XmlFile

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
  RenamePsiElementProcessor.forElement(this)
      .findReferences(this, GlobalSearchScope.projectScope(this.project), false)
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
