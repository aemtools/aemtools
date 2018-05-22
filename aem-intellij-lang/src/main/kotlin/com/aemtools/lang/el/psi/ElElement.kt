package com.aemtools.lang.el.psi

import com.aemtools.lang.el.ElLanguage
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Troynikov
 */
class ElElement(debugName: String)
  : IElementType(debugName, ElLanguage)
