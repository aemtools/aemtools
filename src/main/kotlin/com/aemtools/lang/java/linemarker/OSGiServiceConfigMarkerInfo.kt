package com.aemtools.lang.java.linemarker

import com.aemtools.index.model.OSGiConfiguration
import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.util.Function

/**
 * @author Dmytro_Troynikov
 */
class OSGiServiceConfigMarkerInfo(
    val classIdentifier: PsiIdentifier,
    configs: List<OSGiConfiguration>
) : LineMarkerInfo<PsiElement>(
    classIdentifier,
    classIdentifier.textRange,
    AllIcons.FileTypes.Config,
    Pass.LINE_MARKERS,
    Function { "OSGi configs found" },
    OSGiGutterIconNavigationHandler(configs, classIdentifier, "OSGi Config"),
    GutterIconRenderer.Alignment.CENTER) {
  override fun equals(other: Any?): Boolean {
    val otherMarker = other as? OSGiServiceConfigMarkerInfo
        ?: return false

    return this.classIdentifier.text == otherMarker.classIdentifier.text
  }

  override fun hashCode(): Int {
    return this.classIdentifier.text.hashCode()
  }

}
