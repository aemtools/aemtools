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
    var prevSibling: PsiElement? = this
    while (prevSibling != null && prevSibling.prevSibling != null) {
        prevSibling = prevSibling.prevSibling
        if (prevSibling is CdBasePath) {
            return prevSibling
        }
    }
    return null
}