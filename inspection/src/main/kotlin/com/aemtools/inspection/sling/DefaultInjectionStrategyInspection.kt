package com.aemtools.inspection.sling

import com.aemtools.common.util.findParentByType
import com.aemtools.inspection.common.AemIntellijInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor

/**
 * AEM-16 implementation.
 *
 * @author Dmytro Troynikov
 */
class DefaultInjectionStrategyInspection : AemIntellijInspection(
    groupName ="AEM",
    name = "Default Injection Strategy",
    description = """
       This inspection checks that <i>@Optional</i>
       is not used with <i>defaultInjectionStrategy</i> set
       to <i>OPTIONAL</i>
    """
) {
  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : JavaElementVisitor () {
      override fun visitAnnotation(annotation: PsiAnnotation) {
        if (annotation.qualifiedName == "org.apache.sling.models.annotations.Optional") {
          val containerClass = annotation.findParentByType(PsiClass::class.java) ?: return

          logger<DefaultInjectionStrategyInspection>()
              .debug(null) {
                "Optional found"
              }
        }
      }
    }
  }
}
