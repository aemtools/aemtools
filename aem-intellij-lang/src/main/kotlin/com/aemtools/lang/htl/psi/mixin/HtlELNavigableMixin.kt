package com.aemtools.lang.htl.psi.mixin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.NavigatablePsiElement

/**
 * @author Dmytro Troynikov.
 */
abstract class HtlELNavigableMixin(node: ASTNode)
  : ASTWrapperPsiElement(node), NavigatablePsiElement, ContributedReferenceHost {
  override fun canNavigate(): Boolean = super.canNavigate()

  override fun canNavigateToSource(): Boolean = super.canNavigateToSource()

}
