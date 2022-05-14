package com.aemtools.completion.clientlib.provider

import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.myRelativeDirectory
import com.aemtools.common.util.normalizeToJcrRoot
import com.aemtools.common.util.relativeTo
import com.aemtools.completion.model.psi.SelectedString
import com.aemtools.index.ClientlibDeclarationIndexFacade
import com.aemtools.lang.clientlib.psi.CdInclude
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object ClientlibDeclarationBasePathCompletionProvider : CompletionProvider<CompletionParameters>(), DumbAware {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }
    val currentElement = parameters.position.findParentByType(CdInclude::class.java)
        ?: return
    if (currentElement.absoluteInclude != null || currentElement.relativeInclude != null) {
      handleDirectoryPathInclude(currentElement, parameters, result)
    } else {
      handleSimpleInclude(parameters, result)
    }
  }

  private fun handleDirectoryPathInclude(currentElement: CdInclude,
                                         parameters: CompletionParameters,
                                         result: CompletionResultSet) {
    val baseDirectoryPath = resolveBaseDirectoryPath(currentElement, parameters) ?: return
    val baseDirectoryPathWithTrailingSlash = "$baseDirectoryPath/"

    val groupedSuggestedDirectories = ClientlibDeclarationIndexFacade.findAllTypedFiles(parameters.originalFile)
        .groupBy { getContainingDirectoryPath(it) }
        .filter { it.key.startsWith(baseDirectoryPathWithTrailingSlash) }

    result.addAllElements(groupedSuggestedDirectories.map {
      lookupElement(it.key.replaceFirst(baseDirectoryPathWithTrailingSlash, ""))
          .withTailText("(items: ${it.value.size})")
    })
    result.stopHere()
  }

  private fun resolveBaseDirectoryPath(currentElement: CdInclude,
                                       parameters: CompletionParameters): String? {
    if (currentElement.relativeInclude != null) {
      val relativePathExpression = SelectedString.create(currentElement.relativeInclude?.text)?.value ?: ""
      val baseDirectory = parameters.originalFile.containingDirectory.myRelativeDirectory(relativePathExpression)
      return baseDirectory?.virtualFile?.path?.normalizeToJcrRoot()
    }
    if (currentElement.absoluteInclude != null) {
      val absolutePath = SelectedString.create(currentElement.absoluteInclude?.text)?.value ?: ""
      return absolutePath.substringBeforeLast("/")
    }
    return null
  }

  private fun handleSimpleInclude(parameters: CompletionParameters, result: CompletionResultSet) {
    val suitableDirs = collectSuitableDirs(parameters)
    result.addAllElements(suitableDirs.map {
      lookupElement(it)
    })
    result.stopHere()
  }

  private fun collectSuitableDirs(parameters: CompletionParameters): List<String> {
    val clientlibDeclarationFileName = parameters.originalFile.name
    val containingDirectory = parameters.originalFile.containingDirectory

    fun dirsCollector(dir: PsiDirectory): List<String> {
      val result = ArrayList<String>()
      if (dir.files.any { ClientlibDeclarationIndexFacade.isMatchedByExtension(it, clientlibDeclarationFileName) }) {
        result.add(dir.virtualFile.path.relativeTo(containingDirectory.virtualFile.path))
      }

      dir.subdirectories
          .forEach {
            result.addAll(dirsCollector(it))
          }
      return result
    }

    return containingDirectory.subdirectories
        .flatMap(::dirsCollector)
  }

  private fun getContainingDirectoryPath(it: VirtualFile) = it.parent.path.normalizeToJcrRoot()
}
