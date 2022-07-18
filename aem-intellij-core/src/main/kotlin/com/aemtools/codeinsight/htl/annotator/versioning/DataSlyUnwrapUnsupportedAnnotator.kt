package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.codeinsight.htl.intention.ChangeHtlVersionAction
import com.aemtools.codeinsight.htl.intention.RemoveHtlIdentifierAction
import com.aemtools.codeinsight.htl.intention.RemoveRedundantDataSlyUnwrapValueAction
import com.aemtools.codeinsight.htl.util.notSupportedHtlFeatureAnnotationBuilder
import com.aemtools.common.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.common.util.toSmartPointer
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.getHtlVersion
import com.aemtools.lang.util.htlAttributeName
import com.aemtools.lang.util.isHtlAttribute
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * Annotates data-sly-unwrap identifier and value as not supported in HTL 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class DataSlyUnwrapUnsupportedAnnotator : VersionedHtlElementAnnotator(HtlVersion.V_1_4) {
  override fun annotateNotSupportedElement(element: PsiElement, holder: AnnotationHolder) {
    if (element !is XmlAttribute
        || !element.isHtlAttribute(true)
        || element.htlAttributeName(true) != DATA_SLY_UNWRAP) {
      return
    }

    val htlVariableName = element.name.substringAfterLast(".", "")
    if (htlVariableName.isNotEmpty()) {
      val identifierTextRange = with(element.nameElement.textRange) {
        TextRange(this.endOffset - htlVariableName.length, this.endOffset)
      }
      holder.notSupportedHtlFeatureAnnotationBuilder(element, getMessage(element.project), identifierTextRange)
          .withFix(RemoveHtlIdentifierAction(element.toSmartPointer(), "Remove \"$htlVariableName\" identifier"))
          .create()
    }

    if (element.valueElement != null) {
      val xmlAttributeValue = element.valueElement as PsiElement

      val currentHtlVersion = element.project.getHtlVersion().version
      val message = "This expression has no effect in current HTL version $currentHtlVersion. " +
          "Support for this feature starts with HTL version ${HtlVersion.V_1_4.version}."
      holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
          .range(xmlAttributeValue)
          .withFix(ChangeHtlVersionAction())
          .withFix(RemoveRedundantDataSlyUnwrapValueAction(element.toSmartPointer()))
          .create()
    }
  }
}
