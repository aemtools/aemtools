package com.aemtools.completion.htl.provider

import com.aemtools.codeinsight.htl.model.DeclarationAttributeType
import com.aemtools.codeinsight.htl.model.HtlUseVariableDeclaration
import com.aemtools.codeinsight.htl.model.HtlVariableDeclaration
import com.aemtools.codeinsight.htl.model.UseType
import com.aemtools.common.completion.lookupElement
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtlElDataSlyCallVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    val position = parameters.position
    val fileVariables = FileVariablesResolver.declarationsForPosition(position, parameters)

    val fileTemplates = fileVariables.filter { it.attributeType == DeclarationAttributeType.DATA_SLY_TEMPLATE }
        .map(HtlVariableDeclaration::toLookupElement)

    val useVariables = fileVariables.map { it as? HtlUseVariableDeclaration }
        .filterNotNull()
        .filter { it.slyUseType == UseType.HTL }

    val useTemplates = useVariables
        .map { it to it.template() }
        .filter { (_, value) -> value.isNotEmpty() }
        .flatMap { (key, value) ->
          value.map {
            lookupElement("${key.variableName}.${it.name}")
                .withIcon(HtlIcons.HTL_FILE_ICON)
                .withTypeText("HTL Template")
                .withPresentableText(it.name)
                .withTailText("(${key.xmlAttribute.value})", true)
          }
        }

    result.addAllElements(useTemplates + fileTemplates)
    result.stopHere()
  }

}
