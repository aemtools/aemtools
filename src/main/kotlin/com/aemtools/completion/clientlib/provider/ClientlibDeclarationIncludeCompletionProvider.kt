package com.aemtools.completion.clientlib.provider

import com.aemtools.completion.util.basePathElement
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.getPsi
import com.aemtools.completion.util.relativeTo
import com.aemtools.completion.util.toPsiFile
import com.aemtools.lang.clientlib.CdLanguage
import com.aemtools.lang.clientlib.psi.CdBasePath
import com.aemtools.lang.clientlib.psi.CdInclude
import com.aemtools.lang.clientlib.psi.CdPsiFile
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDirectory
import com.intellij.util.ProcessingContext
import javax.swing.Icon

/**
 * @author Dmytro_Troynikov
 */
object ClientlibDeclarationIncludeCompletionProvider : CompletionProvider<CompletionParameters>(), DumbAware {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val currentElement = parameters.position.findParentByType(CdInclude::class.java)
        ?: return
    val basePath = currentElement.basePathElement()?.include?.text
    val startDir = findStartDirectory(parameters.originalFile.containingDirectory, basePath)
        ?: return

    val rawVariants = collectRawVariants(parameters, startDir, basePath)

    val file = parameters.originalFile.getPsi(CdLanguage) as? CdPsiFile
        ?: return

    val presentVariants = extractPresentVariants(file)

    val filteredVariants = rawVariants.filterNot {
      presentVariants.any { present -> present.realPath == it.realPath }
    }

    result.addAllElements(filteredVariants.map {
      LookupElementBuilder.create(it.relativePath)
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
        currentBasePath = it.text
      } else if (it is CdInclude) {
        if (currentBasePath == null) {
          result.add(CdImport(
              realPath = "$realPath/${it.text}",
              relativePath = it.text
          ))
        } else {
          result.add(CdImport(
              realPath = "$realPath/$currentBasePath/${it.text}",
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
        accept(currentFile.name, it.name)
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

  private fun findStartDirectory(directory: PsiDirectory, basePath: String?): PsiDirectory? {
    if (basePath == null) {
      return directory
    }

    val parts = basePath.split("\\", "/")

    var result: PsiDirectory? = directory
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

  private fun accept(currentFileName: String, fileToCheck: String): Boolean = if (currentFileName == "js.txt") {
    fileToCheck.endsWith(".js")
  } else {
    fileToCheck.endsWith(".css") || fileToCheck.endsWith(".less")
  }

  /**
   * File import data class.
   */
  data class CdImport(
      val realPath: String,
      val relativePath: String,
      val basePath: String? = null,
      val icon: Icon? = null)

}
