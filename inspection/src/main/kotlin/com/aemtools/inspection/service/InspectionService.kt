package com.aemtools.inspection.service

import com.aemtools.common.util.toSmartPointer
import com.aemtools.inspection.html.fix.RemoveRedundantDataSlyUnwrapAction
import com.aemtools.inspection.html.fix.SubstituteWithRawAttributeIntentionAction
import com.aemtools.inspection.message.InspectionMessages.REDUNDANT_DATA_SLY_UNWRAP_MESSAGE
import com.intellij.codeInsight.daemon.impl.analysis.RemoveAttributeIntentionFix
import com.intellij.codeInsight.daemon.impl.quickfix.RemoveRedundantArgumentsFix
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

  override fun redundantDataSlyUnwrap(holder: ProblemsHolder, attribute: XmlAttribute) {
    holder.registerProblem(
        attribute,
        REDUNDANT_DATA_SLY_UNWRAP_MESSAGE,
        ProblemHighlightType.WEAK_WARNING,
        RemoveRedundantDataSlyUnwrapAction(attribute.toSmartPointer())
    )
  }

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
