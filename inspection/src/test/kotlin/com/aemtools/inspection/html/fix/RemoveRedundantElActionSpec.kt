package com.aemtools.inspection.html.fix

import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.test.util.memo
import com.aemtools.test.util.mockComponent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Specification for [RemoveRedundantElAction].
 *
 * @author Dmytro Troynikov
 */
object RemoveRedundantElActionSpec : Spek({
  val htlHtlElPointer: SmartPsiElementPointer<HtlElExpressionMixin> by memo()
  val element: HtlElExpressionMixin by memo()
  val tested: RemoveRedundantElAction by memo {
    RemoveRedundantElAction(htlHtlElPointer)
  }

  on("style check") {
    it("should have correct family") {
      assertThat(tested.familyName)
          .isEqualTo("HTL Intentions")
    }

    it("should have correct text") {
      assertThat(tested.text)
          .isEqualTo("Remove redundant expression.")
    }
  }

  describe("invoke") {
    val project: Project by memo()
    val editor: Editor by memo()
    val psiFile: PsiFile by memo()
    val document: Document by memo()
    val psiDocumentManager: PsiDocumentManager by memo()
    val stringLiteral: HtlStringLiteralMixin by memo()

    beforeEachTest {
      whenever(htlHtlElPointer.element)
          .thenReturn(element)
      whenever(psiDocumentManager.getDocument(psiFile))
          .thenReturn(document)
      whenever(element.textRange)
          .thenReturn(TextRange.create(10, 20))
      whenever(element.children)
          .thenReturn(arrayOf(stringLiteral))
      whenever(stringLiteral.children)
          .thenReturn(emptyArray())
      whenever(stringLiteral.name)
          .thenReturn("com.test.Class")

      mockComponent(project, psiDocumentManager)
    }

    it("should ignore if no element available") {
      whenever(htlHtlElPointer.element)
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
          .getComponent(PsiDocumentManager::class.java)
    }

    it("should ignore if no document available") {
      whenever(psiDocumentManager.getDocument(psiFile))
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(element, never())
          .textRange
    }

    it("should perform string replace if all info is available") {
      tested.invoke(project, editor, psiFile)

      verify(document)
          .replaceString(10, 20, "com.test.Class")
      verify(psiDocumentManager)
          .commitDocument(document)
    }
  }
})
