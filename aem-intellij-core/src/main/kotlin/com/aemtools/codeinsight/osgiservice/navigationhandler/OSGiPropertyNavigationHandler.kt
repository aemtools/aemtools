package com.aemtools.codeinsight.osgiservice.navigationhandler

import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.icons.AllIcons
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.openapi.util.Iconable
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import java.awt.event.MouseEvent
import javax.swing.Icon

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
          (it.containingPsiElement ?: it.containingPsiFile) as NavigatablePsiElement
        }.toTypedArray(),
        "OSGi Property", null, createListCellRenderer(propertyDescriptors))
  }

  private fun createListCellRenderer(propertyDescriptors: List<OSGiPropertyDescriptor>)
      : PsiElementListCellRenderer<PsiElement> {
    return object : PsiElementListCellRenderer<PsiElement>() {
      override fun getIconFlags(): Int = Iconable.ICON_FLAG_READ_STATUS

      override fun getIcon(element: PsiElement): Icon {
        return element.containingFile?.getIcon(iconFlags) ?: AllIcons.Nodes.Variable
      }

      override fun getContainerText(element: PsiElement, name: String): String? {
        return propertyDescriptors.find {
          it.containingPsiElement == element || it.containingPsiFile == element
        }?.propertyValue
      }

      override fun getElementText(element: PsiElement?): String {
        if (element == null) {
          return ""
        }
        return propertyDescriptors.find {
          it.containingPsiElement == element || it.containingPsiFile == element
        }?.mods ?: ""
      }

    }
  }

}
