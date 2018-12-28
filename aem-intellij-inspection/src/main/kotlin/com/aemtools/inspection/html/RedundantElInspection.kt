package com.aemtools.inspection.html

import com.aemtools.common.constant.const
import com.aemtools.common.util.hasChild
import com.aemtools.inspection.common.AemIntellijInspection
import com.aemtools.inspection.service.IInspectionService
import com.aemtools.inspection.service.withServices
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlPropertyAccess
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.visitor.HtlElementVisitor
import com.aemtools.lang.util.isInsideOf
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

/**
 * @author Dmytro Primshyts
 */
class RedundantElInspection : AemIntellijInspection(
    groupName = "HTL",
    name = "Redundant HTL expression",
    description = """
      This inspection checks if HTL expression is used in `data-sly-use` or `data-sly-include` attributes
      without reason. Expression may be required in case if there is necessity to pass some arguments
      via "option" arguments, otherwise expression is redundant
    """.trimIndent()
) {
  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : HtlElementVisitor() {
      override fun visitHtlExpression(htlExpression: HtlElExpressionMixin) {
        withServices(htlExpression.project) { service: IInspectionService ->
          if (hasDefect(htlExpression)) {
            service.reportRedundantEl(htlExpression, holder)
          }
        }
      }
    }
  }

  private fun hasDefect(element: HtlElExpressionMixin): Boolean =
      element.isDumbStringLiteralEl()
          && (element.isInsideOf(const.htl.DATA_SLY_USE) || element.isInsideOf(const.htl.DATA_SLY_INCLUDE))

  /**
   * Check if current current [HtlHtlEl] is a "Dumb String Literal", which mean
   * that the expression doesn't contain anything except the string literal, e.g.:
   * ```
   *   ${'static string'}
   * ```
   */
  private fun HtlElExpressionMixin.isDumbStringLiteralEl(): Boolean =
      this.hasChild(HtlStringLiteral::class.java)
          && !this.hasChild(HtlPropertyAccess::class.java)
          && !this.hasChild(HtlContextExpression::class.java)
}

