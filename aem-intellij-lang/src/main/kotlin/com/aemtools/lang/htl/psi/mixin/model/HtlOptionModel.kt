package com.aemtools.lang.htl.psi.mixin.model

import com.aemtools.lang.htl.psi.HtlContextExpression

/**
 * Helper model for htl option.
 * @author Dmytro Troynikov
 */
class HtlOptionModel(val contextExpression: HtlContextExpression) {

  /**
   * Get name of current option.
   *
   * @return name of current option
   */
  fun name(): String {
    val assignment = contextExpression.assignment
    val variableName = contextExpression.variableName
    if (assignment != null) {
      return assignment.variableName.varName.text
    }
    if (variableName != null) {
      return variableName.varName.text
    }
    return ""
  }

}
