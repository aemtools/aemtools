package com.aemtools.lang.jcrproperty.psi

import com.aemtools.lang.jcrproperty.JcrPropertyLanguage
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class JpToken(debugName: String) : IElementType(debugName, JcrPropertyLanguage) {

  override fun toString(): String = "[Jp] ${super.toString()}"

}
