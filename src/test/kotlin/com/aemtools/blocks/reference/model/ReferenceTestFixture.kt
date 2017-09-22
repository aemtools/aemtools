package com.aemtools.blocks.reference.model

import com.aemtools.blocks.base.model.fixture.TestFixture
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import junit.framework.TestCase

/**
 * @author Dmytro Troynikov
 */
class ReferenceTestFixture(fixture: JavaCodeInsightTestFixture)
  : TestFixture(fixture),
    IReferenceTestFixture {
  var shouldBeResolvedTo: Class<out PsiElement>? = null
  var shouldBeResolvedStrictly: Boolean = false
  var shouldContainText: String? = null

  override fun shouldResolveTo(type: Class<out PsiElement>, strict: Boolean) {
    shouldBeResolvedTo = type
    shouldBeResolvedStrictly = strict
  }

  override fun shouldContainText(text: String) {
    shouldContainText = text
  }

  override fun test() {
    super.test()

    val referenceUnderCaret = fixture.file.findReferenceAt(fixture.editor.caretModel.offset)
        ?: throw AssertionError("Unable to find reference in:\n${fixture.file.text}")

    val resolvedReference = referenceUnderCaret.resolve()

    val _shouldBeResolvedTo = shouldBeResolvedTo
    if (_shouldBeResolvedTo != null) {
      if (shouldBeResolvedStrictly) {
        TestCase.assertEquals(_shouldBeResolvedTo, resolvedReference?.javaClass)
      } else {
        TestCase.assertNotNull("Unable to resolve reference $referenceUnderCaret", resolvedReference)
        TestCase.assertTrue("${_shouldBeResolvedTo.canonicalName} is not assignable from ${resolvedReference?.javaClass?.canonicalName}",
            _shouldBeResolvedTo.isAssignableFrom(resolvedReference?.javaClass))
      }
    }

    if (shouldContainText != null) {
      TestCase.assertEquals(shouldContainText, resolvedReference?.text)
    }
  }

}

