package com.aemtools.codeinsight.osgiservice.markerinfo

import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.codeinsight.osgiservice.navigationhandler.OSGiGutterIconNavigationHandler
import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.util.Function
import java.util.function.Supplier

/**
 * @author Dmytro Primshyts
 */
class OSGiServiceConfigMarkerInfo(
    val classIdentifier: PsiIdentifier,
    configsProvider: () -> List<OSGiConfiguration>
) : LineMarkerInfo<PsiElement>(
    classIdentifier,
    classIdentifier.textRange,
    AllIcons.FileTypes.Config,
    Function { "OSGi configs found" },
    OSGiGutterIconNavigationHandler(configsProvider, classIdentifier, "OSGi Config"),
    GutterIconRenderer.Alignment.CENTER,
    Supplier { "OSGi configs found" }
) {
  override fun equals(other: Any?): Boolean {
    val otherMarker = other as? OSGiServiceConfigMarkerInfo
        ?: return false

    return this.classIdentifier.text == otherMarker.classIdentifier.text
  }

  override fun hashCode(): Int {
    return this.classIdentifier.text.hashCode()
  }

}
