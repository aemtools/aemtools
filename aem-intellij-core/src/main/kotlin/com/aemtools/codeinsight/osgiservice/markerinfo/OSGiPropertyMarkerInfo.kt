package com.aemtools.codeinsight.osgiservice.markerinfo

import com.aemtools.codeinsight.osgiservice.navigationhandler.OSGiPropertyNavigationHandler
import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import java.util.function.Supplier

/**
 * Marker for OSGi properties.
 *
 * @author Dmytro Primshyts
 */
class OSGiPropertyMarkerInfo(
        propertyIdentifier: PsiElement,
        propertyDescriptorsProvider: () -> List<OSGiPropertyDescriptor>
) : LineMarkerInfo<PsiElement>(
        propertyIdentifier,
        propertyIdentifier.textRange,
        AllIcons.Nodes.PropertyRead,
        Function { "OSGi Property" },
        OSGiPropertyNavigationHandler(propertyDescriptorsProvider),
        GutterIconRenderer.Alignment.CENTER,
        Supplier { "OSGi Property" }
)
