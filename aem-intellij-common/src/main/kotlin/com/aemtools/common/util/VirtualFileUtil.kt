package com.aemtools.common.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

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
 * @return the psi directory
 */
fun VirtualFile.toPsiDirectory(project: Project): PsiDirectory? = project.psiManager().findDirectory(this)
