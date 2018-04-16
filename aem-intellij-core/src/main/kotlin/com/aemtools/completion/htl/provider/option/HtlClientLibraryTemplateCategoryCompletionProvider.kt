package com.aemtools.completion.htl.provider.option

import com.aemtools.analysis.htl.callchain
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.common.constant.const
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.withPriority
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object HtlClientLibraryTemplateCategoryCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (result.isStopped) {
      return
    }

    val currentPosition = parameters.position
    val hel = currentPosition.findParentByType(HtlElExpressionMixin::class.java)
        ?: return

    val outputType = hel
        .getMainPropertyAccess()
        ?.callchain()
        ?.getLastOutputType()
        as? TemplateTypeDescriptor
        ?: return

    if (outputType.template.fullName != const.CLIENTLIB_TEMPLATE) {
      return
    }

    val models = HtlIndexFacade.getAllClientLibraryModels(currentPosition.project)

    models.flatMap { clientLibraryModel ->
      clientLibraryModel.categories.map { category ->
        LookupElementBuilder.create(category)
            .withIcon(if (outputType.template.name == "css") {
              AllIcons.FileTypes.Css
            } else {
              AllIcons.FileTypes.JavaScript
            })
            .withPriority(clientLibraryModel.embed.size.toDouble())
      }
    }
        .apply {
          result.addAllElements(this)
          result.stopHere()
        }
  }

}
