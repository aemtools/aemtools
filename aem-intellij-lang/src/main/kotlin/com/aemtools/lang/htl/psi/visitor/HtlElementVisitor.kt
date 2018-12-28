package com.aemtools.lang.htl.psi.visitor

import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.intellij.psi.PsiElementVisitor

/**
 * Htl Psi visitor.
 *
 * @author Dmytro Primshyts
 */
abstract class HtlElementVisitor : PsiElementVisitor() {

  /**
   * Visit [HtlStringLiteral].
   *
   * @param string htl string literal
   */
  open fun visitString(string: HtlStringLiteral) {}

  /**
   * Visit [HtlElExpressionMixin].
   *
   * @param htlExpression htl el expression mixin
   */
  open fun visitHtlExpression(htlExpression: HtlElExpressionMixin) {}

}
