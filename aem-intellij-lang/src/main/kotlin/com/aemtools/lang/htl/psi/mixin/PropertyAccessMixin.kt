package com.aemtools.lang.htl.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

/**
 * @author Dmytro Troynikov
 */
abstract class PropertyAccessMixin(node: ASTNode) : HtlELNavigableMixin(node) {

  override fun getReferences(): Array<PsiReference> {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this)
  }

}
