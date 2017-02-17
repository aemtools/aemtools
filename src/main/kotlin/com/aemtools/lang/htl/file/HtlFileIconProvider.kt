package com.aemtools.lang.htl.file

import com.aemtools.lang.htl.psi.HtlPsiFile
import com.intellij.icons.AllIcons
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
class HtlFileIconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        return if (element is HtlPsiFile) {
            AllIcons.FileTypes.Html // todo add Htl icon
        } else { null }
    }

}