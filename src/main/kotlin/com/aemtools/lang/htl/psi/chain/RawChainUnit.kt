package com.aemtools.lang.htl.psi.chain

import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.intellij.psi.PsiElement
import java.util.LinkedList

/**
 * The call chain unit
 * e.g.
 *
 * ```
 *  ${properties.myProperty} -> [properties, myProperty]
 * ```
 *
 * @author Dmytro_Troynikov
 */
open class RawChainUnit(
    val myCallChain: LinkedList<PsiElement>,
    val myDeclaration: HtlVariableDeclaration? = null) {

  override fun toString(): String {
    return "RawChainUnit(myCallChain=$myCallChain, myDeclaration=$myDeclaration)"
  }

}
