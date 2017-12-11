package com.aemtools.inspection.java

import com.aemtools.common.util.isJavaLangString
import com.aemtools.inspection.service.JavaInspectionService
import com.intellij.codeInspection.BatchSuppressableTool
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.ModuleUtil
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

  fun checkLiteral(psiLiteralExpression: PsiLiteralExpression, holder: ProblemsHolder) {
    if (!psiLiteralExpression.isJavaLangString()) {
      return
    }

    val project = psiLiteralExpression.project
    val javaInspectionService = JavaInspectionService.getInstance(project) ?: return
    val module = ModuleUtil.findModuleForPsiElement(psiLiteralExpression) ?: return
    val literalValue = psiLiteralExpression.value
    val allConstants = javaInspectionService.standardConstants(project, module)

    val filteredConstants = allConstants.filter { constant ->
      constant.value == literalValue
    }

    if (filteredConstants.isNotEmpty()) {
      javaInspectionService.hardcodedConstant(
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
