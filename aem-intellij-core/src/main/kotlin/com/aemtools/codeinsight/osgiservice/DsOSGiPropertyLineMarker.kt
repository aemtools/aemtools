package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyMarkerInfo
import com.aemtools.codeinsight.osgiservice.property.mapper.OSGiComponentPropertyNameMapper
import com.aemtools.codeinsight.osgiservice.property.provider.OSGiPropertyDescriptorsProvider
import com.aemtools.common.constant.const.java.DS_DESIGNATE_ANNOTATION
import com.aemtools.common.constant.const.osgi.DESIGNATE_OCD_ANNOTATION_ATTRIBUTE
import com.aemtools.common.util.*
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.*

/**
 * @author Kostiantyn Diachenko
 */
class DsOSGiPropertyLineMarker : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val identifier = element as? PsiIdentifier
        ?: return null
    val osgiDsConfigClass = identifier.findParentByType(PsiClass::class.java)
        ?: return null
    val osgiConfigMethod = identifier.parent as? PsiMethod
        ?: return null
    if (!osgiDsConfigClass.isDsOSGiConfig() || !osgiConfigMethod.isDsOSGiConfigProperty()) {
      return null
    }

    val referencedOsgiComponent = osgiDsConfigClass.incomingReferences()
        .firstOrNull { isDsOSGiConfigComponent(it.element) }
    val referencedOsgiComponentClass: PsiClass? = if (referencedOsgiComponent != null) {
      referencedOsgiComponent.element.findParentByType(PsiClass::class.java)
    } else {
      findReferencedOsgiComponentClassInCurrentClass(osgiDsConfigClass)
    }

    val referencedOsgiComponentFqn = referencedOsgiComponentClass?.qualifiedName ?: return null
    val configs = OSGiConfigSearch.findConfigsForClass(
        referencedOsgiComponentFqn,
        element.project,
        true)
    if (configs.isEmpty()) {
      return null
    }

    return FelixOSGiPropertyMarkerInfo(element) {
      OSGiPropertyDescriptorsProvider.get(
          referencedOsgiComponentClass,
          OSGiComponentPropertyNameMapper.mapByMethodName(osgiConfigMethod.name)
      )
    }
  }

  private fun isDsOSGiConfigComponent(element: PsiElement?): Boolean {
    val referencedOsgiComponent = element as? PsiJavaCodeReferenceElement
        ?: return false
    val designateAnnotation = referencedOsgiComponent.findParentByType(PsiAnnotation::class.java)
        ?: return false

    if (!designateAnnotation.hasQualifiedName(DS_DESIGNATE_ANNOTATION)) {
      return false
    }

    val attribute = referencedOsgiComponent.findParentByType(PsiNameValuePair::class.java) ?: return false
    return DESIGNATE_OCD_ANNOTATION_ATTRIBUTE == attribute.name
  }

  private fun findReferencedOsgiComponentClassInCurrentClass(configPsiClass: PsiClass): PsiClass? {
    val osgiComponentPsiClass = configPsiClass.parent as? PsiClass ?: return null
    val isReferencedOsgiComponentClass = osgiComponentPsiClass.annotations
        .filter { it.hasQualifiedName(DS_DESIGNATE_ANNOTATION) }
        .flatMap { it.parameterList.attributes.asList() }
        .any { attribute ->
          DESIGNATE_OCD_ANNOTATION_ATTRIBUTE == attribute.name && isOsgiConfig(attribute, configPsiClass)
        }

    if (!isReferencedOsgiComponentClass) {
      return null
    }
    return osgiComponentPsiClass
  }

  private fun isOsgiConfig(attribute: PsiNameValuePair, configPsiClass: PsiClass) =
      attribute.value.findChildrenByType(PsiIdentifier::class.java).any { it.text == configPsiClass.name }

}
