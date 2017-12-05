package com.aemtools.codeinsight.htl.annotator

import com.aemtools.analysis.htl.callchain
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.common.constant.const
import com.aemtools.common.util.findParentByType
import com.aemtools.lang.common.highlight
import com.aemtools.lang.util.isInsideOf
import com.aemtools.lang.util.isOption
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class HtlOptionsAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element !is com.aemtools.lang.htl.psi.mixin.VariableNameMixin
        || !element.isOption()) {
      return
    }

    if (element.isInsideOf(const.htl.DATA_SLY_CALL)) {
      val hel = element.findParentByType(com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin::class.java)
          ?: return

      val outputType = hel
          .getMainPropertyAccess()
          ?.callchain()
          ?.getLastOutputType()
          as? TemplateTypeDescriptor
          ?: return

      val templateParameters = outputType.parameters()
      if (templateParameters.any { it == element.variableName() }) {
        holder.highlight(element, com.aemtools.lang.htl.colorscheme.HtlColors.TEMPLATE_ARGUMENT, "Template Argument")
      }
      return
    }

    if (element.isInsideOf(const.htl.DATA_SLY_TEMPLATE)) {
      holder.highlight(element, com.aemtools.lang.htl.colorscheme.HtlColors.TEMPLATE_PARAMETER, "Template Parameter")
    }

    if (element.isInsideOf(const.htl.DATA_SLY_USE)) {
      return
    }

    if (HtlAttributesRepository.getHtlOptions()
        .any { it.name == element.variableName() }) {
      holder.highlight(element, com.aemtools.lang.htl.colorscheme.HtlColors.STANDARD_OPTION, "Standard Option")
    }
  }

}