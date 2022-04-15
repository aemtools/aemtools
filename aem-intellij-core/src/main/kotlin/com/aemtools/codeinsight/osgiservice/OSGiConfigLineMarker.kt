package com.aemtools.codeinsight.osgiservice

import com.aemtools.index.model.sortByMods
import com.aemtools.index.search.OSGiConfigSearch
import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiServiceConfigMarkerInfo
import com.aemtools.common.util.isOSGiService
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier

/**
 * @author Dmytro Primshyts
 */
class OSGiConfigLineMarker : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    if (element is PsiIdentifier && element.parent is PsiClass) {
      val psiClass = element.parent as? PsiClass
          ?: return null

      if (psiClass.isOSGiService()) {
        val fqn = psiClass.qualifiedName ?: return null
        val configs = OSGiConfigSearch.findConfigsForClass(fqn, element.project, true)
        if (configs.isEmpty()) {
          return null
        }

        return OSGiServiceConfigMarkerInfo(element, configs.sortByMods())
      }
    }
    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>,
                                      result: MutableCollection<in LineMarkerInfo<*>>) {
  }
}
