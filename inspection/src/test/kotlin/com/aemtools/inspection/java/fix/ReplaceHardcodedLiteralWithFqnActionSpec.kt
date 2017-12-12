package com.aemtools.inspection.java.fix

import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.SmartPsiElementPointer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware

/**
 * Specification for [ReplaceHardcodedLiteralWithFqnAction].
 *
 * @author Dmytro Troynikov
 */
object ReplaceHardcodedLiteralWithFqnActionSpec : Spek({
  describe("ReplaceHardcodedLiteralWithFqnAction") {
    val psiLiteral by memoized(CachingMode.TEST) {
      mock<SmartPsiElementPointer<PsiLiteralExpression>>()
    }
    val project: Project by memo()
    val file: PsiFile by memo()
    val editor: Editor by memo()
    val psiDocumentManager: PsiDocumentManager by memo()
    val document: Document by memo()
    val element: PsiLiteralExpression by memo()
    val textRange: TextRange by memo()

    beforeEachTest {
      whenever(element.textRange)
          .thenReturn(textRange)
      whenever(textRange.startOffset)
          .thenReturn(10)
      whenever(textRange.endOffset)
          .thenReturn(20)
      whenever(psiLiteral.element)
          .thenReturn(element)
      whenever(project.getComponent(PsiDocumentManager::class.java))
          .thenReturn(psiDocumentManager)
      whenever(psiDocumentManager.getDocument(file))
          .thenReturn(document)
    }

    val tested by memoized(CachingMode.TEST) {
      ReplaceHardcodedLiteralWithFqnAction(
          "test message",
          ConstantDescriptor(
              "com.test.Class",
              "name",
              "value"
          ),
          psiLiteral
      )
    }

    on("style check") {

      it("should have correct family") {
        assertThat(tested.familyName)
            .isEqualTo("AEM Inspections")
      }

      it("should use message as is") {
        assertThat(tested.text)
            .isEqualTo("test message")
      }
    }

    on("invoke") {
      it("should ignore in case if literal is not available") {
        whenever(psiLiteral.element)
            .thenReturn(null)
        tested.invoke(project, editor, file)

        verify(project, never())
            .getComponent(PsiDocumentManager::class.java)
      }

      it("should ignoer in case if no document found") {
        whenever(psiDocumentManager.getDocument(file))
            .thenReturn(null)

        tested.invoke(project, editor, file)

        verify(element, never())
            .textRange
      }

      it("should replace string if everything fine") {
        tested.invoke(project, editor, file)

        verify(document)
            .replaceString(10, 20, "com.test.Class.name")
        verify(psiDocumentManager)
            .commitDocument(document)
      }
    }

  }
})

private inline fun <reified T> SpecBody.memo(): LifecycleAware<T> {
  return memoized(CachingMode.TEST) {
    com.aemtools.test.util.mock<T>()
  }
}
