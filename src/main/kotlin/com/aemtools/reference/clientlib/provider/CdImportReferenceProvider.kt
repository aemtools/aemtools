package com.aemtools.reference.clientlib.provider

import com.aemtools.completion.util.basePathElement
import com.aemtools.lang.clientlib.psi.CdInclude
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object CdImportReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement,
                                      context: ProcessingContext): Array<PsiReference> {
    val include = element.originalElement as? CdInclude
        ?: return emptyArray()

    val path = addBasePath(include.text, include)

    val file = include.containingFile.containingDirectory?.myRelativeFile(path)

    if (file != null) {
      return arrayOf(PsiFileReference(file, include, TextRange(0, include.textLength)))
    }

    return arrayOf()
  }

  /**
   * Get element relative to current directory.
   *
   * @return relative element, *null* if no such element exists
   */
  private fun PsiDirectory.myRelativeFile(path: String): PsiFile? {
    val normalizedPath = if (path.startsWith("./")) {
      path.substringAfter("./")
    } else {
      path
    }

    when {
      normalizedPath.startsWith("../") ->
        return this.parentDirectory?.myRelativeFile(path.substringAfter("../"))
      normalizedPath.contains("/") -> {
        val subdirName = normalizedPath.split("/")[0]
        val subdir = this.findSubdirectory(subdirName)
            ?: return null
        val subPath = normalizedPath.substring(normalizedPath.indexOf("/") + 1)
        return subdir.myRelativeFile(subPath)
      }
      else -> return this.findFile(normalizedPath)
    }
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
