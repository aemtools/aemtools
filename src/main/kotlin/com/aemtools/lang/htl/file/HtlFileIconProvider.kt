package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.icons.HtlIcons
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
class HtlFileIconProvider : IconProvider() {
  override fun getIcon(element: PsiElement, flags: Int): Icon? {
    return if (element is HtlPsiFile) {
      HtlIcons.HTL_FILE_ICON
    } else {
      null
    }
  }
}
