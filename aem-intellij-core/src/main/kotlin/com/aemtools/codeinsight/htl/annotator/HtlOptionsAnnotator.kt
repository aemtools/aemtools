package com.aemtools.codeinsight.htl.annotator

import com.aemtools.analysis.htl.callchain
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.common.constant.const
import com.aemtools.common.util.createInfoAnnotation
import com.aemtools.common.util.findParentByType
import com.aemtools.lang.htl.colorscheme.HtlColors
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.getHtlVersion
import com.aemtools.lang.util.isInsideOf
import com.aemtools.lang.util.isOption
import com.aemtools.lang.util.supportsHtlVersion
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Primshyts
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
          ?.callchain()
          ?.getLastOutputType()
          as? TemplateTypeDescriptor
          ?: return

      val templateParameters = outputType.parameters()
      if (templateParameters.any { it == element.variableName() }) {
        holder.createInfoAnnotation(element, HtlColors.TEMPLATE_ARGUMENT, "Template Argument")
      }
      return
    }

    if (element.isInsideOf(const.htl.DATA_SLY_TEMPLATE)) {
      holder.createInfoAnnotation(element, HtlColors.TEMPLATE_PARAMETER, "Template Parameter")
    }

    if (element.project.supportsHtlVersion(HtlVersion.V_1_4)
        && listOf(const.htl.DATA_SLY_LIST, const.htl.DATA_SLY_REPEAT).any { element.isBlockSpecificOption(it) }) {
      holder.createInfoAnnotation(element, HtlColors.STANDARD_OPTION, "Iterable Parameter")
      return
    }

    if (element.isBlockSpecificOption(const.htl.DATA_SLY_RESOURCE)) {
      holder.createInfoAnnotation(element, HtlColors.STANDARD_OPTION, "Standard Option")
      return
    }

    if (element.isInsideOf(const.htl.DATA_SLY_USE)) {
      return
    }

    if (HtlAttributesRepository.getHtlOptions(element.project.getHtlVersion())
            .any { it.name == element.variableName() }) {
      holder.createInfoAnnotation(element, HtlColors.STANDARD_OPTION, "Standard Option")
    }
  }

  private fun VariableNameMixin.isBlockSpecificOption(blockName: String): Boolean =
      this.isInsideOf(blockName) &&
          HtlAttributesRepository.getAttributesData(this.project.getHtlVersion())
              .filter { it.name == blockName }
              .flatMap { it.options ?: listOf() }
              .any { it.name == this.variableName() }

}
