package com.aemtools.lang.html.annotation

import com.aemtools.common.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.common.util.toSmartPointer
import com.aemtools.inspection.fix.RemoveRedundantDataSlyUnwrapAction
import com.aemtools.lang.util.isHtlFile
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class RedundantDataSlyUnwrapAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is XmlAttribute
        && element.containingFile.isHtlFile()
        && element.text == DATA_SLY_UNWRAP
        && element.parent?.name?.equals("sly", true) == true) {
      holder.createWarningAnnotation(element, REDUNDANT_DATA_SLY_UNWRAP_MESSAGE)
          .registerFix(RemoveRedundantDataSlyUnwrapAction(element.toSmartPointer()))
    }
  }

  companion object {
    val REDUNDANT_DATA_SLY_UNWRAP_MESSAGE = "The data-sly-unwrap attribute is redundant in sly tag."
  }

}