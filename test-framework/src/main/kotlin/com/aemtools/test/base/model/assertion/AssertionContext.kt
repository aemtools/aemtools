package com.aemtools.test.base.model.assertion

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Primshyts
 */
open class AssertionContext(val fixture: JavaCodeInsightTestFixture)
  : IAssertionContext {
  override fun elementUnderCaret(): PsiElement =
      fixture.file.findElementAt(fixture.caretOffset)
          ?: throw AssertionError("Unable to retrieve element under caret in file: ${fixture.file.text}")

  override fun openedFile(): PsiFile = fixture.file
      ?: throw AssertionError("Fixture hasn't file opened in editor")

}
