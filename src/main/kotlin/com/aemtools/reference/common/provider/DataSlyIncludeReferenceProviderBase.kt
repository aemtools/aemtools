package com.aemtools.reference.common.provider

import com.aemtools.index.HtlIndexFacade
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
abstract class DataSlyIncludeReferenceProviderBase : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
    val name = name(element, context)
        ?: return emptyArray()

    val psiFile = HtlIndexFacade.resolveIncludeFile(name, element.containingFile)
        ?: return emptyArray()

    return arrayOf(
        PsiFileReference(psiFile, element, TextRange(1, element.textLength - 1))
    )
  }

  /**
   * Extract name of included resource.
   *
   * @return the name of included resource
   */
  abstract fun name(element: PsiElement, context: ProcessingContext): String?

}
