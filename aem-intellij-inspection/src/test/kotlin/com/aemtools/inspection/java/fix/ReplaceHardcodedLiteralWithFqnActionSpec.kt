package com.aemtools.inspection.java.fix

import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.aemtools.test.util.mock
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.SmartPsiElementPointer
import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.*

/**
 * Specification for [ReplaceHardcodedLiteralWithFqnAction].
 *
 * @author Dmytro Primshyts
 */
object ReplaceHardcodedLiteralWithFqnActionSpec : ShouldSpec({

  context("style check") {
    val psiLiteralPointer =
      mock<SmartPsiElementPointer<PsiLiteralExpression>>()

    val message = "Test Message"

    val tested = ReplaceHardcodedLiteralWithFqnAction(
      message,
      ConstantDescriptor(
        "com.test.Class",
        "name",
        "value"
      ),
      psiLiteralPointer
    )

    should("have correct family") {
      assertThat(tested.familyName)
        .isEqualTo("AEM Inspections")
    }

    should("should have message unchanged") {
      assertThat(tested.text)
        .isEqualTo(message)
    }
  }

  context("invoke") {
    val psiLiteralExpression =
      mock<PsiLiteralExpression>()

    val project =
      mock<Project>()

    val smartPsiElementPointer =
      mock<SmartPsiElementPointer<PsiLiteralExpression>>()

    val psiDocumentManger =
      mock<PsiDocumentManager>()

    val textRange =
      TextRange.from(10, 10)

    val document =
      mock<Document>()

    val psiFile =
      mock<PsiFile>()

    val editor =
      mock<Editor>()

    val tested =
      ReplaceHardcodedLiteralWithFqnAction(
        "Test Message",
        ConstantDescriptor(
          "com.test.Class",
          "name",
          "value"
        ),
        smartPsiElementPointer
      )


    beforeEach {
      `when`(smartPsiElementPointer.element)
        .thenReturn(psiLiteralExpression)
      `when`(project.getService(PsiDocumentManager::class.java))
        .thenReturn(psiDocumentManger)
      `when`(psiLiteralExpression.textRange)
        .thenReturn(textRange)
      `when`(psiDocumentManger.getDocument(psiFile))
        .thenReturn(document)
    }

    should("ignore if no element available") {
      `when`(smartPsiElementPointer.element)
        .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
        .getService(PsiDocumentManager::class.java)
    }

    should("ignore if no document available") {
      `when`(psiDocumentManger.getDocument(psiFile))
        .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(psiLiteralExpression, never())
        .textRange
    }

    should("replace string correctly if all data in place") {
      tested.invoke(project, editor, psiFile)

      verify(document)
        .replaceString(10, 20, "com.test.Class.name")
      verify(psiDocumentManger)
        .commitDocument(document)
    }
  }

})
