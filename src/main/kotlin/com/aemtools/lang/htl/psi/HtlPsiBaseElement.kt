package com.aemtools.lang.htl.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

/**
 * @author Dmytro Troynikov
 */
open class HtlPsiBaseElement(node: ASTNode) : ASTWrapperPsiElement(node) {

  override fun getReferences(): Array<PsiReference> =
      ReferenceProvidersRegistry.getReferencesFromProviders(this)

}
