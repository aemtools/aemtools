package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.codeinsight.htl.util.notSupportedHtlFeatureAnnotationBuilder
import com.aemtools.lang.htl.psi.HtlRelationalOperator
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement

/**
 * @author Kostiantyn Diachenko
 */
class RelationalOperatorUnsupportedAnnotator : VersionedHtlElementAnnotator(HtlVersion.V_1_4) {
  override fun annotateNotSupportedElement(element: PsiElement, holder: AnnotationHolder) {
    if (element !is HtlRelationalOperator) {
      return
    }

    holder.notSupportedHtlFeatureAnnotationBuilder(element, getMessage(element.project))
        .create()
  }
}
