package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.codeinsight.htl.util.notSupportedHtlFeatureAnnotationBuilder
import com.aemtools.common.constant.const
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.isInsideOf
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement

/**
 * Annotates data-sly-list and data-sly-repeat not supported HTL options in HTL 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class DataSlyIterableOptionsUnsupportedAnnotator : VersionedHtlElementAnnotator(HtlVersion.V_1_4) {
  override fun annotateNotSupportedElement(element: PsiElement, holder: AnnotationHolder) {
    if (element !is HtlContextExpression) {
      return
    }

    if (element.assignment?.variableName?.isBlockSpecificOption(const.htl.DATA_SLY_LIST) == true
        || element.assignment?.variableName?.isBlockSpecificOption(const.htl.DATA_SLY_REPEAT) == true) {

      val variableNameTextRange = element.assignment?.variableName?.textRange ?: element.textRange
      holder.notSupportedHtlFeatureAnnotationBuilder(element, getMessage(element.project), variableNameTextRange)
          .create()
    }
  }

  private fun HtlVariableName.isBlockSpecificOption(blockName: String): Boolean =
      this.isInsideOf(blockName)
          && HtlAttributesRepository.getAttributesData(HtlVersion.V_1_4)
          .filter { it.name == blockName }
          .flatMap { it.options ?: listOf() }
          .any { it.name == this.varName.text }
}
