package com.aemtools.lang.htl.psi.mixin

import com.aemtools.lang.htl.psi.HtlPsiBaseElement
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

/**
 * @author Dmytro_Troynikov
 */
abstract class HtlStringLiteralMixin(node: ASTNode)
  : HtlPsiBaseElement(node), HtlStringLiteral, PsiNamedElement {

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

}
