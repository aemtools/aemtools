package com.aemtools.inspection.html.fix

import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.test.util.mock
import com.aemtools.test.util.mockComponent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.*

/**
 * Specification for [RemoveRedundantElAction].
 *
 * @author Dmytro Primshyts
 */
object RemoveRedundantElActionSpec : ShouldSpec({
  val htlHtlElPointer: SmartPsiElementPointer<HtlElExpressionMixin> = mock()
  val element: HtlElExpressionMixin = mock()
  val tested =
    RemoveRedundantElAction(htlHtlElPointer)

  context("style check") {
    should("have correct family") {
      assertThat(tested.familyName)
        .isEqualTo("HTL Intentions")
    }

    should("have correct text") {
      assertThat(tested.text)
        .isEqualTo("Remove redundant expression.")
    }
  }

  context("invoke") {
    val project: Project = mock()
    val editor: Editor = mock()
    val psiFile: PsiFile = mock()
    val document: Document = mock()
    val psiDocumentManager: PsiDocumentManager = mock()
    val stringLiteral: HtlStringLiteralMixin = mock()

    beforeEach {
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

    should("ignore if no element available") {
      `when`(htlHtlElPointer.element)
        .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
        .getService(PsiDocumentManager::class.java)
    }

    should("ignore if no document available") {
      `when`(psiDocumentManager.getDocument(psiFile))
        .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(element, never())
        .textRange
    }

    should("perform string replace if all info is available") {
      tested.invoke(project, editor, psiFile)

      verify(document)
        .replaceString(10, 20, "com.test.Class")
      verify(psiDocumentManager)
        .commitDocument(document)
    }
  }
})
