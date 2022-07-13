package com.aemtools.codeinsight.htl.annotator.versioning

import com.aemtools.codeinsight.htl.util.notSupportedHtlFeatureText
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.getHtlVersion
import com.aemtools.lang.util.supportsHtlVersion
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * @author Kostiantyn Diachenko
 */
abstract class VersionedHtlElementAnnotator(
    private val sinceVersion: HtlVersion
) : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element.project.supportsHtlVersion(sinceVersion)) {
      return
    }
    annotateNotSupportedElement(element, holder)
  }

  abstract fun annotateNotSupportedElement(element: PsiElement, holder: AnnotationHolder)

  fun getSinceVersion() = sinceVersion

  fun getMessage(project: Project) = notSupportedHtlFeatureText(project.getHtlVersion(), sinceVersion)

}
