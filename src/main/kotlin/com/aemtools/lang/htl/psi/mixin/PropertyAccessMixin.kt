package com.aemtools.lang.htl.psi.mixin

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.rawchainprocessor.RawCallChainProcessor
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.model.declaration.DeclarationAttributeType
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.extractPropertyAccess
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import java.util.LinkedList

/**
 * @author Dmytro Troynikov
 */
abstract class PropertyAccessMixin(node: ASTNode) : HtlELNavigableMixin(node) {

  /**
   * Get call chain from current property access.
   *
   * @return call chain, *null* if no call chain available
   */
  fun callChain(): CallChain? = RawCallChainProcessor.processChain(rawCallChain())

  override fun getReferences(): Array<PsiReference> {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this)
  }

  private fun rawCallChain(): LinkedList<RawChainUnit> {
    var result = LinkedList<RawChainUnit>()
    val myChain = LinkedList(listOf(*this.children))

    val firstElement = myChain.first() as VariableNameMixin
    val firstName = firstElement.variableName()

    val declaration = FileVariablesResolver.findDeclaration(firstName, firstElement)

    if (declaration != null
        && declaration.attributeType !in listOf(
        DeclarationAttributeType.LIST_HELPER,
        DeclarationAttributeType.REPEAT_HELPER,
        DeclarationAttributeType.DATA_SLY_USE
    )
        ) {
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

  private fun createDeclarationChainUnit(declaration: HtlVariableDeclaration): LinkedList<RawChainUnit> {
    val result = LinkedList<RawChainUnit>()

    result.add(RawChainUnit(LinkedList(), declaration))
    return result
  }

}
