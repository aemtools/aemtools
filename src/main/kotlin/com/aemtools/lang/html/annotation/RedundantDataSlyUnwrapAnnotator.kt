package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.hasParent
import com.aemtools.completion.util.toSmartPointer
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.inspection.fix.RemoveRedundantDataSlyUnwrapAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro Troynikov
 */
class RedundantDataSlyUnwrapAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is XmlAttribute
        && element.text == DATA_SLY_UNWRAP
        && element.hasParent {
      it is XmlTag && it.name.equals("sly", true)
    }) {
      holder.createWarningAnnotation(element, REDUNDANT_DATA_SLY_UNWRAP_MESSAGE)
          .registerFix(RemoveRedundantDataSlyUnwrapAction(element.toSmartPointer()))
    }
  }

  companion object {
    val REDUNDANT_DATA_SLY_UNWRAP_MESSAGE = "The data-sly-unwrap attribute is redundant in sly tag."
  }

}
