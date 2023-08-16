package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiPropertyMarkerInfo
import com.aemtools.codeinsight.osgiservice.property.mapper.OSGiComponentPropertyNameMapper
import com.aemtools.codeinsight.osgiservice.property.provider.OSGiPropertyDescriptorsProvider
import com.aemtools.common.constant.const.java.DS_DESIGNATE_ANNOTATION
import com.aemtools.common.constant.const.osgi.DESIGNATE_OCD_ANNOTATION_ATTRIBUTE
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.incomingReferences
import com.aemtools.common.util.isDsOSGiConfig
import com.aemtools.common.util.isDsOSGiConfigProperty
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameValuePair

/**
 * Line marker provider for OSGI (R6, R7) Object Class Definition properties.
 *
 * @author Kostiantyn Diachenko
 */
class OSGiObjectClassDefinitionLineMarker : LineMarkerProvider {
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
        false)
    if (configs.isEmpty()) {
      return null
    }

    return OSGiPropertyMarkerInfo(element) {
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
