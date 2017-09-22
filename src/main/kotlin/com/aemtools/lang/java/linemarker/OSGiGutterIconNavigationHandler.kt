package com.aemtools.lang.java.linemarker

import com.aemtools.index.model.OSGiConfiguration
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiIdentifier
import java.awt.event.MouseEvent

/**
 * @author Dmytro_Troynikov
 */
class OSGiGutterIconNavigationHandler(
    val configs: List<OSGiConfiguration>,
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

  private val messages: Map<String, CellDescriptor> = configs.flatMap {
    val path = it.xmlFile?.virtualFile?.path
    if (path != null) {
      listOf(path to CellDescriptor(
          it.mods.joinToString { it },
          it.suffix()))
    } else {
      listOf()
    }
  }.toMap()

  override fun navigate(e: MouseEvent?, elt: PsiElement?) {
    PsiElementListNavigator.openTargets(e,
        configs.map { it.xmlFile }.toTypedArray(),
        myTitle, null, createListCellRenderer())
  }

  private fun createListCellRenderer(): PsiElementListCellRenderer<PsiFile> {
    return object : PsiElementListCellRenderer<PsiFile>() {
      override fun getIconFlags(): Int {
        return 0
      }

      override fun getElementText(element: PsiFile?): String {
        val path = element?.virtualFile?.path
            ?: return "Unknown"
        return messages[path]?.elementText
            ?: return "Unknown"
      }

      override fun getContainerText(element: PsiFile?, name: String?): String? {
        val path = element?.virtualFile?.path
            ?: return ""
        return messages[path]?.containerText
            ?: ""
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
