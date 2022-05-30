package com.aemtools.completion.htl.provider.option

import com.aemtools.common.util.normalizeToJcrRoot
import com.aemtools.completion.htl.common.SlingResourceTypesCompletionResolver
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtlResourceTypeOptionAssignmentCompletionProvider
  : CompletionProvider<CompletionParameters>() {

  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    val myDirectory = parameters.position.containingFile.originalFile.containingDirectory.virtualFile
        .path

    val myNormalizedDirectory = myDirectory.normalizeToJcrRoot()
    val declarations = SlingResourceTypesCompletionResolver.resolveDeclarations(
        parameters.position.project,
        myNormalizedDirectory
    )
    result.addAllElements(declarations)
    result.stopHere()
  }
}
