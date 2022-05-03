package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiPropertyMarkerInfo
import com.aemtools.codeinsight.osgiservice.property.provider.OSGiPropertyDescriptorsProvider
import com.aemtools.common.constant.const.java.DS_COMPONENT_ANNOTATION
import com.aemtools.common.util.findParentByType
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.*
import com.siyeh.ig.psiutils.ExpressionUtils

/**
 * Line marker provider for OSGI properties.
 *
 * @author Kostiantyn Diachenko
 */
class DsOSGiPropertiesLineMarker : LineMarkerProvider {

  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val expression = toPsiExpression(element) ?: return null
    val osgiDsConfigClass = expression.findParentByType(PsiClass::class.java) ?: return null
    val propertyContainerAttribute = findClosestPropertyContainerAttribute(expression) ?: return null
    findParentComponentAnnotation(propertyContainerAttribute) ?: return null

    val expressionValue = computeOSGiPropertyExpression(expression) ?: return null

    if (!expressionValue.contains("=")) {
      return null
    }
    val propertyName = expressionValue.substringBefore("=").substringBefore(":")

    val referencedOsgiComponentFqn = osgiDsConfigClass.qualifiedName ?: return null
    val configs = OSGiConfigSearch.findConfigsForClass(
        referencedOsgiComponentFqn,
        element.project,
        false)
    if (configs.isEmpty()) {
      return null
    }

    return OSGiPropertyMarkerInfo(element) {
      OSGiPropertyDescriptorsProvider.get(osgiDsConfigClass, propertyName)
    }
  }

  private fun findClosestPropertyContainerAttribute(expression: PsiExpression): PsiNameValuePair? {
    val propertyAnnotationAttribute = expression.findParentByType(PsiNameValuePair::class.java)
    if ("property" != propertyAnnotationAttribute?.name) {
      return null
    }
    return propertyAnnotationAttribute
  }

  private fun findParentComponentAnnotation(annotationAttribute: PsiNameValuePair): PsiAnnotation? {
    val annotation = annotationAttribute.findParentByType(PsiAnnotation::class.java)
        ?: return null
    if (!annotation.hasQualifiedName(DS_COMPONENT_ANNOTATION)) {
      return null
    }
    return annotation
  }

  private fun toPsiExpression(psiElement: PsiElement): PsiExpression? =
      when (psiElement) {
        is PsiIdentifier -> psiElement.parent as? PsiExpression
        is PsiJavaToken -> psiElement.parent as? PsiLiteralExpression
        else -> null
      }

  private fun computeOSGiPropertyExpression(expression: PsiExpression): String? =
      when (expression) {
        is PsiLiteralExpression -> computeLiteralExpression(expression)
        is PsiReferenceExpression -> computePolyadicExpression(expression)
        else -> null
      }

  private fun computePolyadicExpression(expression: PsiReferenceExpression): String? {
    val polyadicExpression = expression.findParentByType(PsiPolyadicExpression::class.java)

    return if (polyadicExpression?.text?.startsWith(expression.text) == true
        && polyadicExpression.text?.endsWith(expression.text) == false
        && ExpressionUtils.computeConstantExpression(expression) != null) {
      ExpressionUtils.computeConstantExpression(polyadicExpression) as? String
    } else {
      null
    }
  }

  private fun computeLiteralExpression(expression: PsiLiteralExpression): String? {
    val polyadicExpression = expression.findParentByType(PsiPolyadicExpression::class.java)
    return if (polyadicExpression == null) {
      expression.text.removeSurrounding("\"")
    } else if (polyadicExpression.text?.startsWith(expression.text) == true) {
      ExpressionUtils.computeConstantExpression(polyadicExpression) as? String
    } else {
      null
    }
  }
}
