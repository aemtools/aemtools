package com.aemtools.completion.util

import com.aemtools.lang.clientlib.psi.CdBasePath
import com.aemtools.lang.clientlib.psi.CdInclude
import com.intellij.psi.PsiElement

/**
 * @author Dmytro_Troynikov
 */

/**
 * Find [CdBasePath] element related to current include.
 *
 * @return related base path element, _null_ if no such element exists
 */
fun CdInclude.basePathElement(): CdBasePath? {
  var element: PsiElement? = this
  return generateSequence {
    element = element?.prevSibling
    element
  }
      .map { it as? CdBasePath }
      .filterNotNull()
      .firstOrNull()
}
