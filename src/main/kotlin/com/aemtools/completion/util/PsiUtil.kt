package com.aemtools.completion.util

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.intellij.lang.Language
import com.intellij.lang.StdLanguages
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.rename.RenamePsiElementProcessor

/**
 * Psi utility & extension methods
 * @author Dmytro_Troynikov
 */

/**
 * Get [PsiFile] for specified [Language].
 * @param language the language
 * @return the [PsiFile] for given [Language], _null_ in case if no file was found
 */
fun PsiFile.getPsi(language: Language): PsiFile? = viewProvider.getPsi(language)

/**
 * Get [PsiFile] for [StdLanguages.HTML] language.
 */
fun PsiFile.getHtmlFile(): PsiFile? = getPsi(StdLanguages.HTML)

/**
 * Get [XmlFile] from current [PsiFile].
 */
fun PsiFile.getXmlFile(): XmlFile? = getPsi(StdLanguages.XML) as? XmlFile

/**
 * Get [HtlPsiFile] from current [PsiFile].
 */
fun PsiFile.getHtlFile(): HtlPsiFile? = getPsi(HtlLanguage) as? HtlPsiFile

/**
 * Convert current [VirtualFile] to [PsiFile].
 * @param project the project
 * @receiver [VirtualFile]
 * @return the psi file
 */
fun VirtualFile.toPsiFile(project: Project): PsiFile? =
        PsiManager.getInstance(project)
                .findFile(this)

/**
 * Convert current [VirtualFile] to [PsiDirectory].
 * @param project the project
 * @receiver [VirtualFile]
 * @return the psi directory]
 */
fun VirtualFile.toPsiDirectory(project: Project): PsiDirectory? =
        PsiManager.getInstance(project)
                .findDirectory(this)

/**
 * Get resource type from current [VirtualFile].
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
 * @return instance of virtual file
 * @see [PsiUtilCore.getVirtualFile]
 */
fun PsiElement.virtualFile(): VirtualFile? =
        PsiUtilCore.getVirtualFile(this)

/**
 * Find incoming references (usages) for current element.
 *
 * @receiver [PsiElement]
 * @return list of incoming references
 */
fun PsiElement.incomingReferences(): List<PsiReference> = runReadAction {
        RenamePsiElementProcessor.forElement(this).findReferences(this)
                .toList()
}
