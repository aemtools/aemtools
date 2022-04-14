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
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.*

/**
 * Specification for [RemoveRedundantElAction].
 *
 * @author Dmytro Primshyts
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
      `when`(htlHtlElPointer.element)
          .thenReturn(element)
      `when`(psiDocumentManager.getDocument(psiFile))
          .thenReturn(document)
      `when`(element.textRange)
          .thenReturn(TextRange.create(10, 20))
      `when`(element.children)
          .thenReturn(arrayOf(stringLiteral))
      `when`(stringLiteral.children)
          .thenReturn(emptyArray())
      `when`(stringLiteral.name)
          .thenReturn("com.test.Class")

      `when`(project.getService(PsiDocumentManager::class.java))
        .thenReturn(psiDocumentManager);
      mockComponent(project, psiDocumentManager)
    }

    it("should ignore if no element available") {
      `when`(htlHtlElPointer.element)
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
          .getService(PsiDocumentManager::class.java)
    }

    it("should ignore if no document available") {
      `when`(psiDocumentManager.getDocument(psiFile))
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
