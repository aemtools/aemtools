package com.aemtools.codeinsight.osgiservice.navigationhandler

import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyDescriptor
import com.aemtools.common.util.toNavigatable
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import java.awt.event.MouseEvent

/**
 * Felix OSGi gutter navigation handler.
 *
 * @author Dmytro Primshyts
 */
class FelixOSGiPropertyNavigationHandler(
    val propertyDescriptors: () -> List<FelixOSGiPropertyDescriptor>
) : GutterIconNavigationHandler<PsiElement> {

  override fun navigate(e: MouseEvent, elt: PsiElement?) {
    PsiElementListNavigator.openTargets(e,
        propertyDescriptors().map {
          (it.xmlAttribute?.toNavigatable() ?: it.osgiConfigFIle) as NavigatablePsiElement
        }.toTypedArray(),
        "OSGi Property", null, createListCellRenderer())
  }

  private fun createListCellRenderer(): PsiElementListCellRenderer<PsiElement> {
    return object : PsiElementListCellRenderer<PsiElement>() {
      override fun getIconFlags(): Int = 0

      override fun getContainerText(element: PsiElement, name: String): String? {
        return propertyDescriptors().find {
          it.xmlAttribute?.toNavigatable() == element
           || it.osgiConfigFIle == element
        }?.propertyValue ?: ""
      }

      override fun getElementText(element: PsiElement?): String {
        if (element == null) {
          return ""
        }
        return propertyDescriptors().find {
          it.xmlAttribute?.toNavigatable() == element
              || it.osgiConfigFIle == element
        }?.mods ?: ""
      }

    }
  }

}
