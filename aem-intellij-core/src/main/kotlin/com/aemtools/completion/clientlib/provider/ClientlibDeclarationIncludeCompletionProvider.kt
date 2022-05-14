package com.aemtools.completion.clientlib.provider

import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.*
import com.aemtools.index.ClientlibDeclarationIndexFacade
import com.aemtools.lang.clientlib.CdLanguage
import com.aemtools.lang.clientlib.psi.CdBasePath
import com.aemtools.lang.clientlib.psi.CdInclude
import com.aemtools.lang.clientlib.psi.CdPsiFile
import com.aemtools.lang.util.basePathElement
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import javax.swing.Icon

/**
 * @author Dmytro Primshyts
 */
object ClientlibDeclarationIncludeCompletionProvider : CompletionProvider<CompletionParameters>(), DumbAware {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val currentElement = parameters.position.findParentByType(CdInclude::class.java)
        ?: return
    val basePath = currentElement.basePathElement()?.include?.text
    val startDir = findStartDirectory(parameters.originalFile, basePath)
        ?: return

    val rawVariants = collectRawVariants(parameters, startDir, basePath)

    val file = parameters.originalFile.getPsi(CdLanguage) as? CdPsiFile
        ?: return

    val presentVariants = extractPresentVariants(file)

    val filteredVariants = rawVariants.filterNot {
      presentVariants.any { present -> present.realPath == it.realPath }
    }

    result.addAllElements(filteredVariants.map {
      lookupElement(it.relativePath)
          .withIcon(it.icon)
    })
    result.stopHere()
  }

  private fun extractPresentVariants(cdFile: CdPsiFile): Collection<CdImport> {
    val elements = cdFile.children.filter {
      it is CdBasePath
          || it is CdInclude
    }
    val realPath: String = cdFile.containingDirectory
        ?.virtualFile
        ?.path
        ?: return emptyList()

    val result: ArrayList<CdImport> = ArrayList()
    var currentBasePath: String? = null
    elements.forEach {
      if (it is CdBasePath) {
        currentBasePath = findStartDirectory(cdFile, it.include?.text)?.virtualFile?.path
      } else if (it is CdInclude) {
        if (currentBasePath == null) {
          result.add(CdImport(
              realPath = "$realPath/${it.text}",
              relativePath = it.text
          ))
        } else {
          result.add(CdImport(
              realPath = "$currentBasePath/${it.text}",
              relativePath = it.text,
              basePath = currentBasePath
          ))
        }
      }
    }
    return result
  }

  private fun collectRawVariants(parameters: CompletionParameters,
                                 startDir: PsiDirectory,
                                 basePath: String?): List<CdImport> {
    val currentFile = parameters.originalFile

    fun variantsCollector(dir: PsiDirectory): List<CdImport> {
      val result = ArrayList<CdImport>()
      dir.files.filter {
        ClientlibDeclarationIndexFacade.isMatchedByExtension(it, currentFile.name)
      }
          .forEach {
            result.add(CdImport(
                it.virtualFile.path,
                it.virtualFile.path.relativeTo(startDir.virtualFile.path),
                basePath,
                it.virtualFile.toPsiFile(parameters.position.project)?.getIcon(0)
            ))
          }
      result.addAll(dir.subdirectories.flatMap(::variantsCollector))
      return result
    }
    return variantsCollector(startDir)
  }

  private fun findStartDirectory(currentFile: PsiFile, basePath: String?): PsiDirectory? {
    if (basePath == null) {
      return currentFile.containingDirectory
    }

    if (isAbsolutePath(basePath)) {
      return getAbsoluteDirectoryPath(currentFile, basePath)?.toPsiDirectory(currentFile.project)
    }

    return getRelativeDirectoryPath(basePath, currentFile)
  }

  private fun getRelativeDirectoryPath(basePath: String, currentFile: PsiFile): PsiDirectory? {
    var result: PsiDirectory? = currentFile.containingDirectory
    val parts = basePath.split("\\", "/")

    parts.forEach {
      when (it) {
        ".." -> result = result?.parentDirectory
        "." -> {
        }
        else -> result = result?.subdirectories?.find { subdirectory -> subdirectory.name == it }
      }
    }

    return result
  }

  private fun getAbsoluteDirectoryPath(file: PsiFile, basePath: String): VirtualFile? {
    return ClientlibDeclarationIndexFacade.findAllTypedFiles(file)
        .map { Pair(it.parent, it.parent.path.normalizeToJcrRoot()) }
        .filter { it.second.startsWith(basePath) }
        .minByOrNull { it.second.replaceFirst(basePath, "").split("/").size }
        ?.let {
          var directory = it.first
          while (directory.path.normalizeToJcrRoot() != basePath) {
            directory = directory.parent
          }
          return directory
        }
  }

  private fun isAbsolutePath(basePath: String) = basePath.startsWith("/")

  /**
   * File import data class.
   */
  data class CdImport(
      val realPath: String,
      val relativePath: String,
      val basePath: String? = null,
      val icon: Icon? = null)

}
