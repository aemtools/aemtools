package com.aemtools.lang.clientlib.psi

import com.aemtools.lang.clientlib.CdLanguage
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro_Troynikov
 */
class CdToken(debugName: String) : IElementType(debugName, CdLanguage) {
  override fun toString(): String = "[Cd] ${super.toString()}"
}
