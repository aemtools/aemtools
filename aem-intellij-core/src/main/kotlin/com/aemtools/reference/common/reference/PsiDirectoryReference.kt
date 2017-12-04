package com.aemtools.reference.common.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

/**
 * @author Dmytro Troynikov
 */
class PsiDirectoryReference(
    val psiDirectory: PsiDirectory,
    element: PsiElement,
    range: TextRange
) : PsiReferenceBase<PsiElement>(element, range) {
  override fun getVariants(): Array<Any> {
    return emptyArray()
  }

  override fun resolve(): PsiElement? {
    return psiDirectory
  }
}
