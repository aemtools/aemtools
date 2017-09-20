package com.aemtools.completion.htl.provider

import com.aemtools.completion.htl.CompletionPriority
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.htl.model.declaration.DeclarationAttributeType
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.util.withPriority
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlElVariableNameCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext?,
                              result: CompletionResultSet) {
    val currentPosition = parameters.position
    val contextObjects = PredefinedVariables.contextObjectsCompletion()
        .map { it.withPriority(CompletionPriority.CONTEXT_OBJECT) }

    val fileVariables = FileVariablesResolver.declarationsForPosition(parameters.position, parameters)
        .filter { it.attributeType != DeclarationAttributeType.DATA_SLY_TEMPLATE }
        .let { variables -> convertToLookupElements(currentPosition, variables) }

    result.addAllElements(fileVariables + contextObjects)
    result.stopHere()
  }

  private fun convertToLookupElements(
      currentPosition: PsiElement,
      variables: List<HtlVariableDeclaration>): List<LookupElement> {
    val result = ArrayList<LookupElement>()

    val outsiders = variables.filter {
      it.xmlAttribute.textOffset > currentPosition.textOffset
    }
    // variables declared after current position should go into the end of the list
    outsiders.mapTo(result) {
      it.toLookupElement().withPriority(CompletionPriority.VARIABLE_OUTSIDER)
    }

    val varsToPrioritize = variables - outsiders

    val weightedVars = varsToPrioritize.map {
      currentPosition.textOffset - it.xmlAttribute.textOffset to it
    }.sortedBy { it.first }

    weightedVars.forEachIndexed { index, pair ->
      val variableDeclaration = pair.second
      val priority = weightedVars.size - 1 - index

      result.add(variableDeclaration
          .toLookupElement()
          .withPriority(CompletionPriority.VARIABLE_BASE + priority))
    }

    return result
  }

}
