package com.aemtools.reference.htl.provider

import com.aemtools.reference.common.provider.DataSlyIncludeReferenceProviderBase
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object DataSlyIncludeElReferenceProvider : DataSlyIncludeReferenceProviderBase() {

  override fun name(element: PsiElement, context: ProcessingContext): String? {
    val literal = element as? com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
    return literal?.name
  }

}
