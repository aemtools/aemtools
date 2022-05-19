package com.aemtools.test.documentation.model

import com.aemtools.test.base.model.fixture.TestFixture
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import junit.framework.TestCase.assertEquals

/**
 * @author Dmytro Primshyts
 */
class DocTestFixture(
    val documentationProvider: AbstractDocumentationProvider,
    fixture: JavaCodeInsightTestFixture)
  : TestFixture(fixture), IDocTestFixture {

  var documentation: String? = null

  override fun documentation(result: String) {
    documentation = result.trimIndent().replace(Regex("\n|\r"), "")
  }

  override fun test() {
    super.test()

    val elementUnderCaret = assertionContext().elementUnderCaret()
    val customDocumentationElement = documentationProvider.getCustomDocumentationElement(
        fixture.editor, fixture.file, elementUnderCaret, fixture.caretOffset)
        ?: elementUnderCaret

    val result = documentationProvider.generateDoc(customDocumentationElement, customDocumentationElement)

    assertEquals(documentation, result)
  }

}
