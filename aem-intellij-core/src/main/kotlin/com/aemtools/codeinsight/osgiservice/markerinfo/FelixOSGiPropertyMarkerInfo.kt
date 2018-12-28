package com.aemtools.codeinsight.osgiservice.markerinfo

import com.aemtools.codeinsight.osgiservice.navigationhandler.FelixOSGiPropertyNavigationHandler
import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.util.Function

/**
 * Marker for Felix OSGi properties.
 *
 * @author Dmytro Primshyts
 */
class FelixOSGiPropertyMarkerInfo(
        propertyIdentifier: PsiIdentifier,
        propertyDescriptorsProvider: () -> List<FelixOSGiPropertyDescriptor>
) : LineMarkerInfo<PsiElement>(
        propertyIdentifier,
        propertyIdentifier.textRange,
        AllIcons.Nodes.PropertyRead,
        Pass.LINE_MARKERS,
        Function { "OSGi Property" },
    FelixOSGiPropertyNavigationHandler(propertyDescriptorsProvider),
        GutterIconRenderer.Alignment.CENTER
)
