package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyMarkerInfo
import com.aemtools.codeinsight.osgiservice.property.provider.OSGiPropertyDescriptorsProvider
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.isFelixProperty
import com.aemtools.common.util.isOSGiService
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiIdentifier

/**
 * @author Dmytro Primshyts
 */
class FelixOSGiPropertyLineMarker : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val identifier = element as? PsiIdentifier
        ?: return null
    val containingClass = identifier.findParentByType(PsiClass::class.java)
        ?: return null
    val field = identifier.parent as? PsiField
        ?: return null

    if (containingClass.isOSGiService() && field.isFelixProperty()) {

      val value = field.computeConstantValue() as? String
          ?: return null

      val containingClassFqn = containingClass.qualifiedName ?: return null

      val configs = OSGiConfigSearch.findConfigsForClass(
          containingClassFqn,
          element.project,
          false)
      if (configs.isEmpty()) {
        return null
      }

      return FelixOSGiPropertyMarkerInfo(element) {
        OSGiPropertyDescriptorsProvider.get(containingClass, value)
      }
    }

    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>,
                                      result: MutableCollection<in LineMarkerInfo<*>>) {
  }

}
