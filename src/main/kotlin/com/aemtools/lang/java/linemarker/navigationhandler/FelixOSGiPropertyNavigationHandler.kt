package com.aemtools.lang.java.linemarker.navigationhandler

import com.aemtools.completion.util.toNavigatable
import com.aemtools.lang.java.linemarker.markerinfo.FelixOSGiPropertyDescriptor
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import java.awt.event.MouseEvent

/**
 * Felix OSGi gutter navigation handler.
 *
 * @author Dmytro Troynikov
 */
class FelixOSGiPropertyNavigationHandler(
        val propertyDescriptors: List<FelixOSGiPropertyDescriptor>
) : GutterIconNavigationHandler<PsiElement> {

    override fun navigate(e: MouseEvent?, elt: PsiElement?) {
        PsiElementListNavigator.openTargets(e,
                propertyDescriptors.map { it.xmlAttribute.toNavigatable() }.toTypedArray(),
                "OSGi Property", null, createListCellRenderer())
    }

    private fun createListCellRenderer(): PsiElementListCellRenderer<XmlAttribute> {
        return object : PsiElementListCellRenderer<XmlAttribute>() {
            override fun getIconFlags(): Int = 0

            override fun getContainerText(element: XmlAttribute, name: String): String? {
                return propertyDescriptors.find {
                    it.xmlAttribute.toNavigatable() == element
                }?.propertyValue ?: ""
            }

            override fun getElementText(element: XmlAttribute?): String {
                if (element == null) {
                    return ""
                }
                return propertyDescriptors.find {
                    it.xmlAttribute.toNavigatable() == element
                }?.mods ?: ""
            }

        }
    }

}