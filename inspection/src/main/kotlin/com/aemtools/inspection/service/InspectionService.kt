package com.aemtools.inspection.service

import com.aemtools.common.util.toSmartPointer
import com.aemtools.inspection.html.fix.SubstituteWithRawAttributeIntentionAction
import com.intellij.codeInsight.daemon.impl.analysis.RemoveAttributeIntentionFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class InspectionService : IInspectionService {

  override fun validTarget(psiElement: PsiElement): Boolean = true

  override fun messedDataSlyAttribute(
      holder: ProblemsHolder,
      attribute: XmlAttribute,
      variableName: String
  ) {
    holder.registerProblem(
        attribute,
        "$variableName is not allowed in data-sly-attribute",
        ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
        RemoveAttributeIntentionFix(
            attribute.name, attribute
        ),
        SubstituteWithRawAttributeIntentionAction(
            attribute.toSmartPointer(),
            "Replace with: $variableName=\"${attribute.value}\""
        )
    )
  }

  companion object {
    fun getInstance(project: Project): IInspectionService {
      return ServiceManager.getService(project, IInspectionService::class.java)
    }
  }

}
