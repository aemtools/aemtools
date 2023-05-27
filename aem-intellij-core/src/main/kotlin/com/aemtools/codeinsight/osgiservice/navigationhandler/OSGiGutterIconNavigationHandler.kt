package com.aemtools.codeinsight.osgiservice.navigationhandler

import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.index.model.sortByMods
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiIdentifier
import com.intellij.util.text.nullize
import java.awt.event.MouseEvent

/**
 * @author Dmytro Primshyts
 */
class OSGiGutterIconNavigationHandler(
    val configsProvider: () -> List<OSGiConfiguration>,
    val classIdentifier: PsiIdentifier,
    val myTitle: String
) : GutterIconNavigationHandler<PsiElement> {
  override fun equals(other: Any?): Boolean {
    val otherGutter = other as? OSGiGutterIconNavigationHandler
        ?: return false
    return classIdentifier.text == otherGutter.classIdentifier.text
  }

  override fun hashCode(): Int {
    return classIdentifier.text.hashCode()
  }

  override fun navigate(e: MouseEvent, elt: PsiElement?) {
    val sortedConfigs = getSortedConfigs()
    PsiElementListNavigator.openTargets(e,
        sortedConfigs.map { it.file }.toTypedArray(),
        myTitle, null, createListCellRenderer(sortedConfigs))
  }

  fun getSortedConfigs() = configsProvider().sortByMods()

  private fun prepareOsgiConfigCellDescriptors(configs: List<OSGiConfiguration>): Map<String, CellDescriptor> =
      configs.flatMap {osgiConfig ->
        val path = osgiConfig.file?.virtualFile?.path
        if (path != null) {
          listOf(path to CellDescriptor(
              osgiConfig.mods.joinToString { it },
              osgiConfig.suffix()))
        } else {
          listOf()
        }
      }.toMap()

  private fun createListCellRenderer(configs: List<OSGiConfiguration>): PsiElementListCellRenderer<PsiFile> {
    val osgiConfigCellDescriptors = prepareOsgiConfigCellDescriptors(configs)

    return object : PsiElementListCellRenderer<PsiFile>() {
      override fun getIconFlags(): Int = Iconable.ICON_FLAG_READ_STATUS

      override fun getElementText(element: PsiFile?): String {
        val path = element?.virtualFile?.path
            ?: return "Unknown"
        return osgiConfigCellDescriptors[path]?.elementText
            ?: return "Unknown"
      }

      override fun getContainerText(element: PsiFile?, name: String?): String? {
        val path = element?.virtualFile?.path
            ?: return null
        return osgiConfigCellDescriptors[path]?.containerText.nullize()
      }

    }
  }

  /**
   * Cell descriptor.
   */
  data class CellDescriptor(
      val elementText: String,
      val containerText: String
  )

}
