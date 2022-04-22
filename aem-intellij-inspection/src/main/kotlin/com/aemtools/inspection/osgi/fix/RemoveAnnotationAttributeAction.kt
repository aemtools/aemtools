package com.aemtools.inspection.osgi.fix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project

/**
 * @author Kostiantyn Diachenko
 */
class RemoveAnnotationAttributeAction(val text: String)
  : LocalQuickFix {

  override fun getFamilyName(): String = text

  override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
    val parent = descriptor.psiElement.parent
    parent.delete()
  }
}
