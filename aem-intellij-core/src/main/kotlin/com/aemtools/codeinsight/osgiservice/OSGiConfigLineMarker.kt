package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiServiceConfigMarkerInfo
import com.aemtools.common.util.isOSGiService
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier

/**
 * Line marker provider for OSGI configs. Supports Felix (SCR) and OSGi (R6, R7) components.
 *
 * @author Dmytro Primshyts
 */
class OSGiConfigLineMarker : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    if (element is PsiIdentifier && element.parent is PsiClass) {
      val psiClass = element.parent as? PsiClass
          ?: return null

      if (psiClass.isOSGiService()) {
        val fqn = psiClass.qualifiedName ?: return null
        val configs = OSGiConfigSearch.findConfigsForClass(fqn, element.project, false)
        if (configs.isEmpty()) {
          return null
        }

        return OSGiServiceConfigMarkerInfo(element) {
          OSGiConfigSearch.findConfigsForClass(fqn, element.project, true)
        }
      }
    }
    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>,
                                      result: MutableCollection<in LineMarkerInfo<*>>) {
  }
}
