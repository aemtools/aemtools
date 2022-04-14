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
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.mockito.Mockito.*

/**
 * Specification for [ReplaceHardcodedLiteralWithFqnAction].
 *
 * @author Dmytro Primshyts
 */
object ReplaceHardcodedLiteralWithFqnActionSpec : Spek({

  on("style check") {
    val psiLiteralPointer by memoized {
      mock<SmartPsiElementPointer<PsiLiteralExpression>>()
    }

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

    it("should have correct family") {
      assertThat(tested.familyName)
          .isEqualTo("AEM Inspections")
    }

    it("should have message unchanged") {
      assertThat(tested.text)
          .isEqualTo(message)
    }
  }

  describe("invoke") {
    val psiLiteralExpression by memoized(CachingMode.TEST) {
      mock<PsiLiteralExpression>()
    }
    val project by memoized(CachingMode.TEST) {
      mock<Project>()
    }
    val smartPsiElementPointer by memoized(CachingMode.TEST) {
      mock<SmartPsiElementPointer<PsiLiteralExpression>>()
    }

    val psiDocumentManger by memoized(CachingMode.TEST) {
      mock<PsiDocumentManager>()
    }

    val textRange by memoized(CachingMode.TEST) {
      TextRange.from(10, 10)
    }

    val document by memoized(CachingMode.TEST) {
      mock<Document>()
    }

    val psiFile by memoized(CachingMode.TEST) {
      mock<PsiFile>()
    }

    val editor by memoized(CachingMode.TEST) {
      mock<Editor>()
    }

    val tested by memoized(CachingMode.TEST) {
      ReplaceHardcodedLiteralWithFqnAction(
          "Test Message",
          ConstantDescriptor(
              "com.test.Class",
              "name",
              "value"
          ),
          smartPsiElementPointer
      )
    }

    beforeEachTest {
      `when`(smartPsiElementPointer.element)
          .thenReturn(psiLiteralExpression)
      `when`(project.getService(PsiDocumentManager::class.java))
          .thenReturn(psiDocumentManger)
      `when`(psiLiteralExpression.textRange)
          .thenReturn(textRange)
      `when`(psiDocumentManger.getDocument(psiFile))
          .thenReturn(document)
    }

    it("should ignore if no element available") {
      `when`(smartPsiElementPointer.element)
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
          .getService(PsiDocumentManager::class.java)
    }

    it ("should ignore if no document available") {
      `when`(psiDocumentManger.getDocument(psiFile))
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(psiLiteralExpression, never())
          .textRange
    }

    it("should replace string correctly if all data in place") {
      tested.invoke(project, editor, psiFile)

      verify(document)
          .replaceString(10, 20, "com.test.Class.name")
      verify(psiDocumentManger)
          .commitDocument(document)
    }
  }

})
