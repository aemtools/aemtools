package com.aemtools.inspection.java

import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.aemtools.inspection.service.IInspectionService
import com.aemtools.inspection.service.IJavaInspectionService
import com.aemtools.test.util.memo
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiLiteralExpression
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.ArgumentMatchers.anyList
import org.picocontainer.PicoContainer

/**
 * Specification for [AemConstantInspection].
 *
 * @author Dmytro Primshyts
 */
object AemConstantInspectionSpec : Spek({
  val tested = AemConstantInspection()

  on("style check") {
    it("should have correct group display name") {
      assertThat(tested.groupDisplayName)
          .isEqualTo("AEM")
    }

    it("should have correct display name") {
      assertThat(tested.displayName)
          .isEqualTo("Hardcoded AEM specific literal")
    }

    it("should have correct static description") {
      assertThat(tested.staticDescription)
          .isEqualTo("""
<html>
<body>
This inspection verifies that predefined AEM constants are used instead of
hardcode.
</body>
</html>""".trimIndent())
    }
  }
  describe("check literal") {
    val psiLiteralExpression: PsiLiteralExpression by memo()
    val project: Project by memo()
    val inspectionService: IInspectionService by memo()
    val javaInspectionService: IJavaInspectionService by memo()
    val module: Module by memo()
    val problemsHolder: ProblemsHolder by memo()
    val picoContainer: PicoContainer by memo()

    beforeEachTest {
      whenever(picoContainer.getComponentInstance(IJavaInspectionService::class.java.name))
          .thenReturn(javaInspectionService)
      whenever(picoContainer.getComponentInstance(IInspectionService::class.java.name))
          .thenReturn(inspectionService)
      whenever(project.picoContainer)
          .thenReturn(picoContainer)
      whenever(psiLiteralExpression.project)
          .thenReturn(project)
      whenever(inspectionService.validTarget(any()))
          .thenReturn(true)
      whenever(javaInspectionService.isJavaLangString(any()))
          .thenReturn(true)
      whenever(psiLiteralExpression.value)
          .thenReturn("com.test.Bean")
      whenever(inspectionService.moduleForPsiElement(psiLiteralExpression))
          .thenReturn(module)

      whenever(javaInspectionService.standardConstants(project, module))
          .thenReturn(listOf(
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
          ))
    }

    it("should return if target is invalid") {
      whenever(inspectionService.validTarget(psiLiteralExpression))
          .thenReturn(false)

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService, never())
          .reportHardcodedConstant(
              any(),
              any(),
              anyList()
          )

    }

    it("should return if literal is not `java.lang.String`") {
      whenever(javaInspectionService.isJavaLangString(psiLiteralExpression))
          .thenReturn(false)

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService, never())
          .reportHardcodedConstant(
              any(),
              any(),
              anyList()
          )
    }

    it("should return if no module was found") {
      whenever(inspectionService.moduleForPsiElement(psiLiteralExpression))
          .thenReturn(null)

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService, never())
          .reportHardcodedConstant(
              any(),
              any(),
              anyList()
          )
    }

    it("should report matched standard constant") {
      whenever(psiLiteralExpression.value)
          .thenReturn("value1")

      tested.checkLiteral(psiLiteralExpression, problemsHolder)
      verify(javaInspectionService)
          .reportHardcodedConstant(
              problemsHolder,
              psiLiteralExpression,
              listOf(
                  ConstantDescriptor("com.test.Constants1",
                      "Name1",
                      "value1")
              )
          )
    }

  }

})
