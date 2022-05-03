package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiPropertyMarkerInfo
import com.aemtools.codeinsight.osgiservice.property.provider.OSGiPropertyDescriptorsProvider
import com.aemtools.common.constant.const
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.isFelixProperty
import com.aemtools.common.util.isOSGiService
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.*

/**
 * Line marker provider for SCR properties.
 *
 * @author Dmytro Primshyts
 */
class FelixOSGiPropertyLineMarker : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val identifier = element as? PsiIdentifier
        ?: return null
    val containingClass = identifier.findParentByType(PsiClass::class.java)
        ?: return null
    val psiElement = getFieldOrAnnotationAttributePsiElement(identifier) ?: return null

    if (containingClass.isOSGiService() && isFelixProperty(psiElement)) {

      val value = extractPropertyName(psiElement) ?: return null

      val containingClassFqn = containingClass.qualifiedName ?: return null

      val configs = OSGiConfigSearch.findConfigsForClass(
          containingClassFqn,
          element.project,
          false)
      if (configs.isEmpty()) {
        return null
      }

      return OSGiPropertyMarkerInfo(element) {
        OSGiPropertyDescriptorsProvider.get(containingClass, value)
      }
    }

    return null
  }

  private fun getFieldOrAnnotationAttributePsiElement(identifier: PsiIdentifier): PsiElement? =
      when (identifier.parent) {
        is PsiField -> identifier.parent
        is PsiNameValuePair -> if ("name" == identifier.text) {
          identifier.parent
        } else {
          null
        }
        else -> null
      }

  private fun isFelixProperty(psiElement: PsiElement): Boolean =
      when (psiElement) {
        is PsiField -> psiElement.isFelixProperty()
        is PsiNameValuePair -> isInnerFelixProperty(psiElement)
        else -> false
      }

  private fun isInnerFelixProperty(annotationAttribute: PsiNameValuePair): Boolean {
    val psiAnnotation = annotationAttribute.findParentByType(PsiAnnotation::class.java)
    if (psiAnnotation != null && psiAnnotation.hasQualifiedName(const.java.FELIX_PROPERTY_ANNOTATION)) {
      return psiAnnotation.findParentByType(PsiAnnotation::class.java, true)
          ?.hasQualifiedName(const.java.FELIX_PROPERTIES_ANNOTATION) ?: false
    }
    return false
  }

  private fun extractPropertyName(identifierParentElement: PsiElement): String? =
      when (identifierParentElement) {
        is PsiField -> identifierParentElement.computeConstantValue() as? String
        is PsiNameValuePair -> getPropertyNameFromAnnotation(identifierParentElement)
        else -> null
      }

  private fun getPropertyNameFromAnnotation(annotation: PsiNameValuePair): String? =
      when (annotation.value) {
        is PsiReferenceExpression -> {
          val psiField = annotation.value?.reference?.resolve() as? PsiField
          psiField?.computeConstantValue() as? String
        }
        is PsiLiteralExpression -> annotation.literalValue
        else -> null
      }

  override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>,
                                      result: MutableCollection<in LineMarkerInfo<*>>) {
  }

}
