package com.aemtools.lang.htl.psi.visitor

import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.intellij.psi.PsiElementVisitor

/**
 * Htl Psi visitor.
 *
 * @author Dmytro Troynikov
 */
abstract class HtlElementVisitor : PsiElementVisitor() {

  open fun visitString(string: HtlStringLiteral) {}

  open fun visitHtlExpression(htlExpression: HtlElExpressionMixin) {}

}
