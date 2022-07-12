package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.codeinsight.htl.intention.RemoveHtlIdentifierAction
import com.aemtools.codeinsight.htl.intention.RemoveRedundantDataSlyUnwrapValueAction
import com.aemtools.codeinsight.htl.util.notSupportedHtlFeatureAnnotationBuilder
import com.aemtools.common.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.common.util.toSmartPointer
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.htlAttributeName
import com.aemtools.lang.util.htlVariableName
import com.aemtools.lang.util.isHtlAttribute
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Kostiantyn Diachenko
 */
class DataSlyUnwrapUnsupportedAnnotator : VersionedHtlElementAnnotator(HtlVersion.V_1_4) {
  override fun annotateNotSupportedElement(element: PsiElement, holder: AnnotationHolder) {
    if (element !is XmlAttribute
        || !element.isHtlAttribute()
        || element.htlAttributeName() != DATA_SLY_UNWRAP) {
      return
    }

    val htlVariableName = element.htlVariableName()
    if (htlVariableName != null) {
      val identifierTextRange = with(element.nameElement.textRange) {
        TextRange(this.endOffset - htlVariableName.length, this.endOffset)
      }
      holder.notSupportedHtlFeatureAnnotationBuilder(element, getMessage(element.project), identifierTextRange)
          .withFix(RemoveHtlIdentifierAction(element.toSmartPointer(), "Remove $htlVariableName identifier"))
          .create()
    }

    if (element.valueElement != null) {
      val xmlAttributeValue = element.valueElement as PsiElement
      holder.notSupportedHtlFeatureAnnotationBuilder(xmlAttributeValue)
          .withFix(RemoveRedundantDataSlyUnwrapValueAction(element.toSmartPointer()))
          .create()
    }
  }
}
