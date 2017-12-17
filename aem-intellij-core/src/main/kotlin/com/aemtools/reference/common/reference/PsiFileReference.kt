package com.aemtools.reference.common.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference

/**
 * Reference to PsiFile.
 *
 * @author Dmytro Troynikov
 */
class PsiFileReference(val psiFile: PsiElement?,
                       holder: PsiElement,
                       range: TextRange) : PsiReferenceBase<PsiElement>(holder, range, true),
    PsiFileReference {

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {

    return arrayOf(object : ResolveResult {
      override fun getElement(): PsiElement? {
        return psiFile
      }

      override fun isValidResult(): Boolean {
        return true
      }

    })
  }

  override fun resolve(): PsiElement? {
    return psiFile
  }

  override fun getVariants(): Array<out Any> {
    return arrayOf()
  }

}
