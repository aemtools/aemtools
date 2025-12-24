package com.aemtools.inspection.html.fix

import com.aemtools.test.util.mock
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute
import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.*

/**
 * Specification for [RemoveRedundantDataSlyUnwrapAction].
 *
 * @author Dmytro Primshyts
 */
object RemoveRedundantDataSlyUnwrapActionSpec : ShouldSpec({
  val xmlAttributePointer: SmartPsiElementPointer<XmlAttribute> = mock()
  val xmlAttribute: XmlAttribute = mock()
  val psiDocumentManager: PsiDocumentManager = mock()
  val project: Project = mock()
  val document: Document = mock()
  val psiFile: PsiFile = mock()
  val editor: Editor = mock()
  val tested =
    RemoveRedundantDataSlyUnwrapAction(
      xmlAttributePointer
    )


  context("style check") {
    should("have correct family") {
      assertThat(tested.familyName)
        .isEqualTo("HTL Intentions")
    }
    should("have correct text") {
      assertThat(tested.text)
        .isEqualTo("Remove attribute.")
    }
  }

  context("invoke") {
    beforeEach {
      `when`(xmlAttributePointer.element)
        .thenReturn(xmlAttribute)
      `when`(project.getService(PsiDocumentManager::class.java))
        .thenReturn(psiDocumentManager)
      `when`(psiDocumentManager.getDocument(psiFile))
        .thenReturn(document)
      `when`(xmlAttribute.textRange)
        .thenReturn(TextRange.create(10, 20))
    }

    should("ignore if no element available") {
      `when`(xmlAttributePointer.element)
        .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
        .getService(PsiDocumentManager::class.java)
    }

    should("ignore if no document available") {
      `when`(psiDocumentManager.getDocument(psiFile))
        .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(xmlAttribute, never())
        .textRange
    }

    should("replace if everything fine") {
      tested.invoke(project, editor, psiFile)

      verify(document)
        .replaceString(10, 20, "")
      verify(psiDocumentManager)
        .commitDocument(document)
    }
  }

})
