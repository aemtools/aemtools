package com.aemtools.reference.clientlib.provider

import com.aemtools.index.ClientlibDeclarationIndexFacade
import com.aemtools.lang.clientlib.psi.CdInclude
import com.aemtools.lang.util.basePathElement
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object CdImportReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement,
                                      context: ProcessingContext): Array<PsiReference> {
    val include = element.originalElement as? CdInclude
        ?: return emptyArray()

    val path = addBasePath(include.text, include)

    val file = ClientlibDeclarationIndexFacade.findFileByPath(path, include.containingFile)

    if (file != null) {
      return arrayOf(PsiFileReference(file, include, TextRange(0, include.textLength)))
    }

    return arrayOf()
  }

  private fun addBasePath(path: String, include: CdInclude): String {
    val basePath = include.basePathElement()?.include?.text
    return if (basePath != null) {
      "$basePath/$path"
    } else {
      path
    }
  }

}
