package com.aemtools.inspection.java

import com.aemtools.inspection.service.InspectionService
import com.aemtools.inspection.service.JavaInspectionService
import com.intellij.codeInspection.BatchSuppressableTool
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiLiteralExpression

/**
 * @author Dmytro Troynikov
 */
class AemConstantInspection : LocalInspectionTool(), BatchSuppressableTool {

  override fun getGroupDisplayName(): String = "AEM"

  override fun getDisplayName(): String = "Hardcoded AEM specific literal"

  override fun getStaticDescription(): String? {
    return """
<html>
<body>
This inspection verifies that predefined AEM constants are used instead of
hardcode.
</body>
</html>
    """.trimIndent()
  }

  /**
   * Check if given [PsiLiteralExpression] contains string contained in one of
   * OOTB platform constant-holder classes.
   * If any replacement candidates for hardcoded literal is found, will delegate
   * problem reporting to [JavaInspectionService].
   *
   * @param psiLiteralExpression literal to inspect
   * @param holder the problems holder
   */
  fun checkLiteral(psiLiteralExpression: PsiLiteralExpression, holder: ProblemsHolder) {
    val project = psiLiteralExpression.project
    val inspectionService = InspectionService.getInstance(project) ?: return
    val javaInspectionService = JavaInspectionService.getInstance(project) ?: return

    if (!inspectionService.validTarget(psiLiteralExpression)
        || !javaInspectionService.isJavaLangString(psiLiteralExpression)) {
      return
    }

    val literalValue = psiLiteralExpression.value
    val module = inspectionService.moduleForPsiElement(psiLiteralExpression)
        ?: return

    val allConstants = javaInspectionService.standardConstants(project, module)

    val filteredConstants = allConstants.filter { constant ->
      constant.value == literalValue
    }

    if (filteredConstants.isNotEmpty()) {
      javaInspectionService.reportHardcodedConstant(
          holder,
          psiLiteralExpression,
          filteredConstants)
    }
  }

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : JavaElementVisitor() {
      override fun visitLiteralExpression(expression: PsiLiteralExpression) {
        checkLiteral(expression, holder)
      }
    }
  }

}
