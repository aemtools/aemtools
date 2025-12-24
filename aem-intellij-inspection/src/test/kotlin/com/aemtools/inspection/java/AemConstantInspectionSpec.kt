package com.aemtools.inspection.java

import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.aemtools.inspection.service.IInspectionService
import com.aemtools.inspection.service.IJavaInspectionService
import com.aemtools.test.util.mock
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiLiteralExpression
import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.*
import org.mockito.kotlin.any

/**
 * Specification for [AemConstantInspection].
 *
 * @author Dmytro Primshyts
 */
object AemConstantInspectionSpec : ShouldSpec({
  val tested = AemConstantInspection()

  context("style check") {
    should("have correct group display name") {
      assertThat(tested.groupDisplayName)
        .isEqualTo("AEM")
    }

    should("have correct display name") {
      assertThat(tested.displayName)
        .isEqualTo("Hardcoded AEM specific literal")
    }

    should("have correct static description") {
      assertThat(tested.staticDescription)
        .isEqualTo(
          """
<html>
<body>
This inspection verifies that predefined AEM constants are used instead of
hardcode.
</body>
</html>""".trimIndent()
        )
    }
  }
  context("check literal") {
    val psiLiteralExpression: PsiLiteralExpression = mock()
    val project: Project = mock()
    val inspectionService: IInspectionService = mock()
    val javaInspectionService: IJavaInspectionService = mock()
    val module: Module = mock()
    val problemsHolder: ProblemsHolder = mock()

    beforeEach {
      `when`(project.getService(IJavaInspectionService::class.java))
        .thenReturn(javaInspectionService)
      `when`(project.getService(IInspectionService::class.java))
        .thenReturn(inspectionService)
      `when`(psiLiteralExpression.project)
        .thenReturn(project)
      `when`(inspectionService.validTarget(psiLiteralExpression))
        .thenReturn(true)
      `when`(javaInspectionService.isJavaLangString(psiLiteralExpression))
        .thenReturn(true)
      `when`(psiLiteralExpression.value)
        .thenReturn("com.test.Bean")
      `when`(inspectionService.moduleForPsiElement(psiLiteralExpression))
        .thenReturn(module)

      `when`(javaInspectionService.standardConstants(project, module))
        .thenReturn(
          listOf(
            ConstantDescriptor(
              "com.test.Constants1",
              "Name1",
              "value1"
            ),
            ConstantDescriptor(
              "com.test.Constants2",
              "Name2",
              "value2"
            )
          )
        )
    }

    should("return if target is invalid") {
      `when`(inspectionService.validTarget(psiLiteralExpression))
        .thenReturn(false)

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService, never())
        .reportHardcodedConstant(
          any(),
          any(),
          anyList()
        )

    }

    should("return if literal is not `java.lang.String`") {
      `when`(javaInspectionService.isJavaLangString(psiLiteralExpression))
        .thenReturn(false)

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService, never())
        .reportHardcodedConstant(
          any(),
          any(),
          anyList()
        )
    }

    should("return if no module was found") {
      `when`(inspectionService.moduleForPsiElement(psiLiteralExpression))
        .thenReturn(null)

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService, never())
        .reportHardcodedConstant(
          any(),
          any(),
          anyList()
        )
    }

    should("report matched standard constant") {
      `when`(psiLiteralExpression.value)
        .thenReturn("value1")

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService)
        .reportHardcodedConstant(
          problemsHolder,
          psiLiteralExpression,
          listOf(
            ConstantDescriptor(
              "com.test.Constants1",
              "Name1",
              "value1"
            )
          )
        )
    }

  }

})
