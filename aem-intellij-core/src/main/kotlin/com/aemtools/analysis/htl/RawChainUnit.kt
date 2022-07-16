package com.aemtools.analysis.htl

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.rawchainprocessor.RawCallChainProcessor
import com.aemtools.codeinsight.htl.model.DeclarationAttributeType
import com.aemtools.codeinsight.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.util.extractHtlHel
import com.aemtools.lang.util.extractPropertyAccess
import com.intellij.psi.PsiElement
import java.util.*

/**
 * The call chain unit
 * e.g.
 *
 * ```
 *  ${properties.myProperty} -> [properties, myProperty]
 * ```
 *
 * @author Dmytro Primshyts
 */
open class RawChainUnit(
    val myCallChain: LinkedList<PsiElement>,
    val myDeclaration: HtlVariableDeclaration? = null) {

  override fun toString(): String {
    return "RawChainUnit(myCallChain=$myCallChain, myDeclaration=$myDeclaration)"
  }

}

/**
 * Extract list of [RawChainUnit]'s from current [PropertyAccessMixin].
 *
 * @receiver [PropertyAccessMixin]
 * @return list of raw chain units
 */
fun PropertyAccessMixin.rawCallChain(): LinkedList<RawChainUnit> {
  var result = LinkedList<RawChainUnit>()

  val myChain: LinkedList<PsiElement> = LinkedList(listOf(*this.children).filterIsInstance<VariableNameMixin>())

  val firstElement = myChain.first() as VariableNameMixin
  val firstName = firstElement.variableName()

  val declaration = FileVariablesResolver.findDeclaration(firstName, firstElement)

  if (declaration != null
      && declaration.attributeType !in listOf(
      DeclarationAttributeType.LIST_HELPER,
      DeclarationAttributeType.REPEAT_HELPER,
      DeclarationAttributeType.DATA_SLY_USE
  )) {
    val propertyAccessMixin = declaration.xmlAttribute
        .extractHtlHel()?.extractPropertyAccess()

    // if property access mixin is available recursively obtain it's call chain
    if (propertyAccessMixin != null) {
      result = propertyAccessMixin.rawCallChain()
    }

    if (propertyAccessMixin == null) {
      createDeclarationChainUnit(declaration)
    }
  }

  val myChainUnit = RawChainUnit(myChain, declaration)

  return LinkedList(listOf(*result.toTypedArray(), myChainUnit))
}

/**
 * Get call chain from current property access mixin.
 *
 * @receiver [PropertyAccessMixin]
 * @return call chain, *null* if no call chain available
 */
fun PropertyAccessMixin.callchain(): CallChain? = RawCallChainProcessor.processChain(rawCallChain())

private fun createDeclarationChainUnit(declaration: HtlVariableDeclaration): LinkedList<RawChainUnit> {
  return LinkedList(listOf(RawChainUnit(LinkedList(), declaration)))
}
