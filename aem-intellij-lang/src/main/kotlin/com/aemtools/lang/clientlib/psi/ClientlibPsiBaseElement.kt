package com.aemtools.lang.clientlib.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

/**
 * @author Dmytro Primshyts
 */
open class ClientlibPsiBaseElement(node: ASTNode) : ASTWrapperPsiElement(node) {

  override fun getReferences(): Array<PsiReference> =
      ReferenceProvidersRegistry.getReferencesFromProviders(this)

}
