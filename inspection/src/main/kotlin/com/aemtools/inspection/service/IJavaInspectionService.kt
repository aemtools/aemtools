package com.aemtools.inspection.service

import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiLiteralExpression

/**
 * @author Dmytro Troynikov
 */
interface IJavaInspectionService {

  /**
   * Get list of *all* ootb constants.
   *
   * @param project the project
   * @return list of all available ootb constants
   */
  fun standardConstants(project: Project, module: Module): List<ConstantDescriptor>

  /**
   * Register "hardcoded constant problem".
   *
   * @param holder the problems holder
   * @param literal the problematic literal
   * @param constantDescriptors constant descriptors
   */
  fun reportHardcodedConstant(
      holder: ProblemsHolder,
      literal: PsiLiteralExpression,
      constantDescriptors: List<ConstantDescriptor>)

  /**
   * Check if given [PsiLiteralExpression] is `java.lang.String`.
   *
   * @param psiLiteralExpression literal to check
   * @return *true* if given literal is `lava.lang.String`,
   * *false* otherwise
   */
  fun isJavaLangString(
      psiLiteralExpression: PsiLiteralExpression
  ) : Boolean

}
