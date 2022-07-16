package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.codeinsight.htl.intention.ReplaceDataSlySetWithDataSlyTestAction
import com.aemtools.codeinsight.htl.util.notSupportedHtlFeatureAnnotationBuilder
import com.aemtools.common.constant.const.htl.DATA_SLY_SET
import com.aemtools.common.util.toSmartPointer
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.htlAttributeName
import com.aemtools.lang.util.isHtlAttribute
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.refactoring.suggested.startOffset

/**
 * Annotates data-sly-set as supported HTL attribute in HTL 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class DataSlySetUnsupportedAnnotator : VersionedHtlElementAnnotator(HtlVersion.V_1_4) {
  override fun annotateNotSupportedElement(element: PsiElement, holder: AnnotationHolder) {
    if (element !is XmlAttribute || !element.isHtlAttribute(true)) {
      return
    }

    val htlAttributeName = element.htlAttributeName(true) ?: return
    if (htlAttributeName == DATA_SLY_SET) {
      val textRange = with(element.nameElement.startOffset) {
        TextRange.create(this, this + htlAttributeName.length)
      }
      holder.notSupportedHtlFeatureAnnotationBuilder(element, getMessage(element.project), textRange)
          .withFix(ReplaceDataSlySetWithDataSlyTestAction(element.toSmartPointer()))
          .create()
    }
  }
}
