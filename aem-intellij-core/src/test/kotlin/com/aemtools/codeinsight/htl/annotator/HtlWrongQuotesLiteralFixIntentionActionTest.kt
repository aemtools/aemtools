package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.test.junit.MockitoExtension
import com.intellij.openapi.components.ComponentManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
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

    project.mockComponent(psiDocumentManager)

    tested = HtlWrongQuotesLiteralFixIntentionAction(pointer)
  }

  private fun ComponentManager.mockComponent(component: Any) {
    whenever(getComponent(component.javaClass))
        .thenReturn(component)
  }

  @Test
  fun testFormat() {
    assertThat(tested.text)
        .isEqualTo("Invert HTL Literal quotes.")

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

}
