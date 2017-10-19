package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.psi.PsiElement

/**
 * Call chain element interface.
 *
 * @author Dmytro_Troynikov
 */
interface CallChainElement {
  val element: PsiElement
  val name: String
  val type: TypeDescriptor
}
