package com.aemtools.inspection.html.fix

import com.aemtools.test.util.memo
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.runner.RunWith
import org.mockito.Mockito.*

/**
 * Specification for [RemoveRedundantDataSlyUnwrapAction].
 *
 * @author Dmytro Primshyts
 */
object RemoveRedundantDataSlyUnwrapActionSpec : Spek({
  val xmlAttributePointer: SmartPsiElementPointer<XmlAttribute> by memo()
  val xmlAttribute: XmlAttribute by memo()
  val psiDocumentManager: PsiDocumentManager by memo()
  val project: Project by memo()
  val document: Document by memo()
  val psiFile: PsiFile by memo()
  val editor: Editor by memo()
  val tested by memo {
    RemoveRedundantDataSlyUnwrapAction(
        xmlAttributePointer
    )
  }

  on("style check") {
    it("should have correct family") {
      assertThat(tested.familyName)
          .isEqualTo("HTL Intentions")
    }
    it("should have correct text") {
      assertThat(tested.text)
          .isEqualTo("Remove attribute.")
    }
  }

  describe("invoke") {
    beforeEachTest {
      `when`(xmlAttributePointer.element)
          .thenReturn(xmlAttribute)
      `when`(project.getService(PsiDocumentManager::class.java))
          .thenReturn(psiDocumentManager)
      `when`(psiDocumentManager.getDocument(psiFile))
          .thenReturn(document)
      `when`(xmlAttribute.textRange)
          .thenReturn(TextRange.create(10, 20))
    }

    it("should ignore if no element available") {
      `when`(xmlAttributePointer.element)
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(project, never())
          .getService(PsiDocumentManager::class.java)
    }

    it("should ignore if no document available") {
      `when`(psiDocumentManager.getDocument(psiFile))
          .thenReturn(null)

      tested.invoke(project, editor, psiFile)

      verify(xmlAttribute, never())
          .textRange
    }

    it("should replace if everything fine") {
      tested.invoke(project, editor, psiFile)

      verify(document)
          .replaceString(10, 20, "")
      verify(psiDocumentManager)
          .commitDocument(document)
    }
  }

})
