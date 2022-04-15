package com.aemtools.lang.clientlib.psi

import com.aemtools.lang.clientlib.CdLanguage
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class CdElement(debugName: String)
  : IElementType(debugName, CdLanguage) {
  override fun toString() = "[Cd] ${super.toString()}"
}
