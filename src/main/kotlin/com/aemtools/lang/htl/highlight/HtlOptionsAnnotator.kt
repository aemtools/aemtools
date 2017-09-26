package com.aemtools.lang.htl.highlight

import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isInsideOf
import com.aemtools.completion.util.isOption
import com.aemtools.constant.const
import com.aemtools.lang.common.highlight
import com.aemtools.lang.htl.colorscheme.HtlColors
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class HtlOptionsAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element !is VariableNameMixin
        || !element.isOption()) {
      return
    }

    if (element.isInsideOf(const.htl.DATA_SLY_CALL)) {
      val hel = element.findParentByType(HtlElExpressionMixin::class.java)
          ?: return

      val outputType = hel
          .getMainPropertyAccess()
          ?.callChain()
          ?.getLastOutputType()
          as? TemplateTypeDescriptor
          ?: return

      val templateParameters = outputType.parameters()
      if (templateParameters.any { it == element.variableName() }) {
        holder.highlight(element, HtlColors.TEMPLATE_ARGUMENT, "Template Argument")
      }
      return
    }

    if (element.isInsideOf(const.htl.DATA_SLY_TEMPLATE)) {
      holder.highlight(element, HtlColors.TEMPLATE_PARAMETER, "Template Parameter")
    }

    if (element.isInsideOf(const.htl.DATA_SLY_USE)) {
      return
    }

    if (HtlAttributesRepository.getHtlOptions()
        .any { it.name == element.variableName() }) {
      holder.highlight(element, HtlColors.STANDARD_OPTION, "Standard Option")
    }
  }

}
