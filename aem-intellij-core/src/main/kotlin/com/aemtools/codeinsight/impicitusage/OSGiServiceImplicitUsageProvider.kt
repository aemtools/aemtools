package com.aemtools.codeinsight.impicitusage

import com.aemtools.common.constant.Const.Java.FELIX_SERVICE_REFERENCE_ANNOTATION
import com.aemtools.common.constant.Const.Java.OSGI_SERVICE_REFERENCE_ANNOTATION
import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMember

class OSGiServiceImplicitUsageProvider : ImplicitUsageProvider {
  override fun isImplicitWrite(element: PsiElement): Boolean {
    return when (element) {
      is PsiField -> annotatedWithOsgiReferenceAnnotations(element)
      else -> false
    }
  }

  override fun isImplicitUsage(element: PsiElement): Boolean {
    return isImplicitRead(element) || isImplicitWrite(element)
  }

  override fun isImplicitRead(element: PsiElement): Boolean {
    return false
  }

  private fun annotatedWithOsgiReferenceAnnotations(psiField: PsiMember): Boolean =
    OSGI_REFERENCE_ANNOTATIONS.any { psiField.hasAnnotation(it) }

  companion object {
    val OSGI_REFERENCE_ANNOTATIONS: List<String> =
      listOf(FELIX_SERVICE_REFERENCE_ANNOTATION, OSGI_SERVICE_REFERENCE_ANNOTATION)
  }
}
