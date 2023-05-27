package com.aemtools.codeinsight.osgiconfig

import com.aemtools.common.constant.const.JCR_PRIMARY_TYPE
import com.aemtools.common.constant.const.xml.SLING_OSGI_CONFIG
import com.aemtools.common.util.hasAttribute
import com.aemtools.common.util.xmlAttributeMatcher
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import javax.swing.DefaultListCellRenderer

/**
 * @author Dmytro Primshyts
 */
class XmlOSGiConfigGotoClassLineMarkerProvider : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val xmlTag = element as? XmlTag ?: return null

    if (xmlTag hasAttribute xmlAttributeMatcher(
            name = JCR_PRIMARY_TYPE,
            value = SLING_OSGI_CONFIG
        )) {
      val fileName = xmlTag.containingFile.name

      val className = fileName.substringBeforeLast(".")
          .substringBefore("-")

      val serviceClass = JavaSearch.findClass(className, xmlTag.project)
          ?: return null

      return LineMarkerInfo(
          xmlTag.firstChild,
          xmlTag.firstChild.textRange,
          AllIcons.FileTypes.JavaClass,
          { "Open associated OSGi service" },
          { mouseEvent, _ ->
            PsiElementListNavigator.openTargets(
                mouseEvent,
                arrayOf(serviceClass),
                "Open associated OSGi service",
                null,
                DefaultListCellRenderer()
            )
          },
          GutterIconRenderer.Alignment.CENTER,
          { "Open associated OSGi service" }
      )
    }

    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>,
                                      result: MutableCollection<in LineMarkerInfo<*>>) {
//    super.collectSlowLineMarkers(elements, result)
  }

}
