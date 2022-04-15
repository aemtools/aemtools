package com.aemtools.inspection.sling

import com.aemtools.common.constant.const
import com.aemtools.common.util.annotations
import com.aemtools.common.util.findParentByType
import com.aemtools.inspection.common.AemIntellijInspection
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.RemoveAnnotationQuickFix
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor

/**
 * AEM-16 implementation.
 *
 * Will report `@Optional` annotation as "unused" on `@Inject`'ed field in
 * case if `@Model` annotation has `defaultInjectionStrategy = OPTIONAL`.
 *
 * @author Dmytro Primshyts
 */
class DefaultInjectionStrategyInspection : AemIntellijInspection(
    groupName = "AEM",
    name = "Default Injection Strategy",
    description = """
       This inspection checks that <i>@Optional</i>
       is not used with <i>defaultInjectionStrategy</i> set
       to <i>OPTIONAL</i>
    """
) {
  private fun checkAnnotation(annotation: PsiAnnotation,
                              containerClass: PsiClass,
                              holder: ProblemsHolder) {
    val modelAnnotation = containerClass.annotations()
        .find {
          it.qualifiedName == const.java.SLING_MODEL
        } ?: return

    val injectionStrategy = modelAnnotation.parameterList.attributes.find { nameValuePair ->
      nameValuePair.name == "defaultInjectionStrategy"
    } ?: return

    if (injectionStrategy.value?.text?.contains("OPTIONAL") == true) {
      holder.registerProblem(
          annotation,
          "Redundant annotation",
          ProblemHighlightType.LIKE_UNUSED_SYMBOL,
          RemoveAnnotationQuickFix(
              annotation, null
          )
      )
    }
  }

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : JavaElementVisitor() {
      override fun visitAnnotation(annotation: PsiAnnotation) {
        if (annotation.qualifiedName == const.java.OPTIONAL) {
          val containerClass = annotation.findParentByType(PsiClass::class.java) ?: return
          checkAnnotation(
              annotation,
              containerClass,
              holder
          )
        }
      }
    }
  }
}
