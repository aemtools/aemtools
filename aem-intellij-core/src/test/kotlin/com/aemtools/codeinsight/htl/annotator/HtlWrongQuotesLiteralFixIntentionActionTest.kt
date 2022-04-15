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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*

/**
 * Test for [HtlWrongQuotesLiteralFixIntentionAction].
 *
 * @author Dmytro Primshyts
 */
@ExtendWith(MockitoExtension::class)
class HtlWrongQuotesLiteralFixIntentionActionTest {

  @Mock
  lateinit var pointer: SmartPsiElementPointer<HtlStringLiteralMixin>
  @Mock
  lateinit var literal: HtlStringLiteralMixin
  @Mock
  lateinit var project: Project
  @Mock
  lateinit var editor: Editor
  @Mock
  lateinit var file: PsiFile
  @Mock
  lateinit var psiDocumentManager: PsiDocumentManager
  @Mock
  lateinit var document: Document

  lateinit var tested: HtlWrongQuotesLiteralFixIntentionAction

  @BeforeEach
  fun init() {
    `when`(pointer.element)
      .thenReturn(literal)

    `when`(project.getService(PsiDocumentManager::class.java))
      .thenReturn(psiDocumentManager)

    `when`(psiDocumentManager.getDocument(file))
      .thenReturn(document)

    `when`(literal.text)
      .thenReturn("''")
    `when`(literal.textRange)
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
    `when`(pointer.element)
      .thenReturn(null)

    tested.invoke(project, editor, file)

    verify(project, never())
      .getComponent(PsiDocumentManager::class.java)
  }

  @Test
  fun `should return if document is null`() {
    `when`(psiDocumentManager.getDocument(file))
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
