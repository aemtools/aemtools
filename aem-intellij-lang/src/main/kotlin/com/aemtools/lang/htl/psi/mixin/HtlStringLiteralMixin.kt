package com.aemtools.lang.htl.psi.mixin

import com.aemtools.lang.htl.psi.HtlPsiBaseElement
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.visitor.HtlElementVisitor
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement

/**
 * @author Dmytro_Troynikov
 */
abstract class HtlStringLiteralMixin(node: ASTNode)
  : HtlPsiBaseElement(node), HtlStringLiteral, PsiNamedElement, PsiNameIdentifierOwner {

  override fun accept(visitor: PsiElementVisitor) {
    if (visitor is HtlElementVisitor) {
      return visitor.visitString(this)
    } else {
      super.accept(visitor)
    }
  }

  override fun setName(name: String): PsiElement {
    return this
  }

  override fun getName(): String {
    return if (this.text.length <= 2) {
      return ""
    } else {
      this.text.substring(1, this.text.length - 1)
    }
  }

  override fun getNameIdentifier() = this

}
