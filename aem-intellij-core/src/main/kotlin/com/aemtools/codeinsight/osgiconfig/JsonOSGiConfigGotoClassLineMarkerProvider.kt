package com.aemtools.codeinsight.osgiconfig

import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.icons.AllIcons
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import javax.swing.DefaultListCellRenderer

class JsonOSGiConfigGotoClassLineMarkerProvider : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val jsonObject = element as? JsonObject ?: return null

    val fileName = jsonObject.containingFile.name

    val className = fileName.substringBeforeLast(".cfg.json")
        .substringBefore("-")

    val serviceClass = JavaSearch.findClass(className, jsonObject.project)
        ?: return null

    return LineMarkerInfo(
        jsonObject.firstChild,
        jsonObject.firstChild.textRange,
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
}
