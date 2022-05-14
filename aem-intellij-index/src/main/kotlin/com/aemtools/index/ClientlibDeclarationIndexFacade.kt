package com.aemtools.index

import com.aemtools.common.constant.const
import com.aemtools.common.util.myRelativeFile
import com.aemtools.common.util.normalizeToJcrRoot
import com.aemtools.common.util.toPsiFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.PathUtil

/**
 * @author Kostiantyn Diachenko
 */
object ClientlibDeclarationIndexFacade {
  private val CSS_FILES_EXTENSIONS = listOf("css", "less")
  private val JS_FILES_EXTENSIONS = listOf("js")

  fun findAllTypedFiles(psiFile: PsiFile): List<VirtualFile> {
    val project = psiFile.project
    val suggestionFileExtensions = resolveFileExtensions(psiFile.name)

    return suggestionFileExtensions
        .flatMap { FilenameIndex.getAllFilesByExt(project, it, GlobalSearchScope.projectScope(project)) }
        .filter { it.path.contains(const.JCR_ROOT_SEPARATED) }
  }

  fun findFileByPath(path: String, containingFile: PsiFile): PsiFile? {
    return if (isAbsolutePath(path)) {
      findFileByAbsolutePath(path, containingFile.project)?.toPsiFile(containingFile.project)
    } else {
      findFileByRelativePath(containingFile, path)
    }
  }

  private fun isAbsolutePath(path: String) = path.startsWith("/")

  private fun findFileByAbsolutePath(path: String, project: Project): VirtualFile? {
    return PathUtil.getFileExtension(path)?.let { extension ->
      FilenameIndex.getAllFilesByExt(project, extension, GlobalSearchScope.projectScope(project))
          .find { it.path.normalizeToJcrRoot() == path }
    }
  }

  private fun findFileByRelativePath(containingFile: PsiFile, path: String) =
      containingFile.containingDirectory?.myRelativeFile(path)

  fun isMatchedByExtension(fileToCheck: PsiFile, clientlibDeclarationFileName: String) =
      resolveFileExtensions(clientlibDeclarationFileName)
          .any { fileExtension -> fileToCheck.name.endsWith(fileExtension) }

  private fun resolveFileExtensions(currentFileName: String): List<String> =
      when (currentFileName) {
        "js.txt" -> JS_FILES_EXTENSIONS
        "css.txt" -> CSS_FILES_EXTENSIONS
        else -> emptyList()
      }
}
