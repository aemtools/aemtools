package com.aemtools.blocks.documentation.model

import com.aemtools.blocks.base.model.fixture.TestFixture
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import junit.framework.TestCase.assertEquals

/**
 * @author Dmytro Troynikov
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

    val result = documentationProvider.generateDoc(elementUnderCaret, elementUnderCaret)

    assertEquals(documentation, result)
  }

}
