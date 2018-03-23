package com.aemtools.lang.htl.psi

import com.aemtools.lang.htl.HtlLanguage
import com.intellij.psi.tree.IElementType

/**
 * Htl token.
 */
class HtlToken(debugName: String)
  : IElementType(debugName, HtlLanguage) {
  override fun toString() = "[Htl] ${super.toString()}"
}

/**
 * Base htl element.
 */
class HtlElement(debugName: String)
  : IElementType(debugName, HtlLanguage) {
  override fun toString() = "[Htl] ${super.toString()}"
}
