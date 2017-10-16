package com.aemtools.codeinsight.osgiconfig

import com.aemtools.completion.util.hasAttribute
import com.aemtools.completion.util.xmlAttributeMatcher
import com.aemtools.constant.const.JCR_PRIMARY_TYPE
import com.aemtools.constant.const.xml.SLING_OSGI_CONFIG
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.Function
import javax.swing.DefaultListCellRenderer

/**
 * @author Dmytro Troynikov
 */
class OSGiConfigGotoClassLineMarkerProvider : LineMarkerProvider {
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
          Pass.LINE_MARKERS,
          Function { "Open associated OSGi service" },
          GutterIconNavigationHandler { mouseEvent, _ ->
            PsiElementListNavigator.openTargets(
                mouseEvent,
                arrayOf(serviceClass),
                "Open associated OSGi service",
                null,
                DefaultListCellRenderer()
            )
          },
          GutterIconRenderer.Alignment.CENTER
      )
    }

    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<PsiElement>, result: MutableCollection<LineMarkerInfo<PsiElement>>) {

  }

}
