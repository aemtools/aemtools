package com.aemtools.completion.htl.provider.option

import com.aemtools.analysis.htl.callchain
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.findParentByType
import com.aemtools.completion.htl.inserthandler.HtlElAssignmentInsertHandler
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtlDataSlyCallOptionCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet) {
    val currentPosition = parameters.position
    val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
        ?: return

    val outputType = hel
        .getMainPropertyAccess()
        ?.callchain()
        ?.getLastOutputType()
        as? TemplateTypeDescriptor
        ?: return

    val templateParameters = outputType.parameters()

    val presentOptions = hel.getOptions()
        .map { it.name() }
        .filterNot { it == "" }

    val variants = templateParameters
        .filterNot { presentOptions.contains(it) }
        .map {
          lookupElement(it)
              .withIcon(AllIcons.Nodes.Parameter)
              .withTypeText("HTL Template Parameter")
              .withInsertHandler(HtlElAssignmentInsertHandler())
        }

    result.addAllElements(variants)
    result.stopHere()
  }
}
