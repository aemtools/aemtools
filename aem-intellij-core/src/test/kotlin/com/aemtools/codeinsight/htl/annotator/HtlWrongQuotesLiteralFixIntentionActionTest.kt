package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.test.junit.MockitoExtension
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock

/**
 * Test for [HtlWrongQuotesLiteralFixIntentionAction].
 *
 * @author Dmytro Troynikov
 */
@ExtendWith(MockitoExtension::class)
class HtlWrongQuotesLiteralFixIntentionActionTest {

  @Mock lateinit var pointer: SmartPsiElementPointer<HtlStringLiteralMixin>
  @Mock lateinit var literal: HtlStringLiteralMixin
  @Mock lateinit var project: Project
  @Mock lateinit var editor: Editor
  @Mock lateinit var file: PsiFile
  @Mock lateinit var psiDocumentManager: PsiDocumentManager
  @Mock lateinit var document: Document

  lateinit var tested: HtlWrongQuotesLiteralFixIntentionAction

  @BeforeEach
  fun init() {
    whenever(pointer.element)
        .thenReturn(literal)

    whenever(project.getComponent(PsiDocumentManager::class.java))
        .thenReturn(psiDocumentManager)

    whenever(psiDocumentManager.getDocument(file))
        .thenReturn(document)

    whenever(literal.text)
        .thenReturn("''")
    whenever(literal.textRange)
        .thenReturn(TextRange(0, 2))

    tested = HtlWrongQuotesLiteralFixIntentionAction(pointer)
  }

  @Test
  fun testFormat() {
    assertThat(tested.text)
        .isEqualTo("Invert HTL Literal quotes")

    assertThat(tested.familyName)
        .isEqualTo("HTL Intentions")

    assertThat(tested.name)
        .isEqualTo("HTL Intentions")
  }

  @Test
  fun `should return if element is null`() {
    whenever(pointer.element)
        .thenReturn(null)

    tested.invoke(project, editor, file)

    verify(project, never())
        .getComponent(PsiDocumentManager::class.java)
  }

  @Test
  fun `should return if document is null`() {
    whenever(psiDocumentManager.getDocument(file))
        .thenReturn(null)

    tested.invoke(project, editor, file)

    verify(literal, never())
        .text
  }

  @Test
  fun `should swap quotes if everything is fine`() {
    tested.invoke(project, editor, file)

    verify(document)
        .replaceString(eq(0), eq(2), eq("\"\""))
    verify(psiDocumentManager)
        .commitDocument(document)
  }

}
