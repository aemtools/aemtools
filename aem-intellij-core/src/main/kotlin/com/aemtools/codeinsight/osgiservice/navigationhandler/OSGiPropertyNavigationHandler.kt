package com.aemtools.codeinsight.osgiservice.navigationhandler

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiPropertyDescriptor
import com.aemtools.common.util.toNavigatable
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import java.awt.event.MouseEvent

/**
 * OSGi gutter navigation handler.
 *
 * @author Dmytro Primshyts
 */
class OSGiPropertyNavigationHandler(
    val propertyDescriptors: () -> List<OSGiPropertyDescriptor>
) : GutterIconNavigationHandler<PsiElement> {

  override fun navigate(e: MouseEvent, elt: PsiElement?) {
    val propertyDescriptors = propertyDescriptors()
    PsiElementListNavigator.openTargets(e,
        propertyDescriptors.map {
          (it.xmlAttribute?.toNavigatable() ?: it.osgiConfigFIle) as NavigatablePsiElement
        }.toTypedArray(),
        "OSGi Property", null, createListCellRenderer(propertyDescriptors))
  }

  private fun createListCellRenderer(propertyDescriptors: List<OSGiPropertyDescriptor>)
      : PsiElementListCellRenderer<PsiElement> {
    return object : PsiElementListCellRenderer<PsiElement>() {
      override fun getIconFlags(): Int = 0

      override fun getContainerText(element: PsiElement, name: String): String? {
        return propertyDescriptors.find {
          it.xmlAttribute?.toNavigatable() == element
              || it.osgiConfigFIle == element
        }?.propertyValue ?: ""
      }

      override fun getElementText(element: PsiElement?): String {
        if (element == null) {
          return ""
        }
        return propertyDescriptors.find {
          it.xmlAttribute?.toNavigatable() == element
              || it.osgiConfigFIle == element
        }?.mods ?: ""
      }

    }
  }

}
