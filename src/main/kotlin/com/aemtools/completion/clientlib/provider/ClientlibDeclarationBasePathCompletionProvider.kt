package com.aemtools.completion.clientlib.provider

import com.aemtools.completion.util.relativeTo
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDirectory
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object ClientlibDeclarationBasePathCompletionProvider : CompletionProvider<CompletionParameters>(), DumbAware {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val suitableDirs = collectSuitableDirs(parameters)
    result.addAllElements(suitableDirs.map {
      LookupElementBuilder.create(it)
    })
    result.stopHere()
  }

  private fun collectSuitableDirs(parameters: CompletionParameters): List<String> {
    val myName = parameters.originalFile.name
    val containingDirectory = parameters.originalFile.containingDirectory

    fun dirsCollector(dir: PsiDirectory): List<String> {
      val result = ArrayList<String>()
      if (dir.files.any {
        if (myName == "js.txt") {
          jsTxtSuitableFileMatcher(it.name)
        } else {
          cssTxtSuitableFileMatcher(it.name)
        }
      }) {
        result.add(dir.virtualFile.path.relativeTo(containingDirectory.virtualFile.path))
      }

      dir.subdirectories
          .forEach {
            result.addAll(dirsCollector(it))
          }
      return result
    }

    val result = containingDirectory.subdirectories
        .flatMap(::dirsCollector)
    return result
  }

  private fun jsTxtSuitableFileMatcher(name: String): Boolean = name.endsWith("js")

  private fun cssTxtSuitableFileMatcher(name: String): Boolean = name.endsWith("css") || name.endsWith("less")
}
