package com.aemtools.test.pattern

import com.aemtools.common.constant.const
import com.aemtools.common.util.getHtmlFile
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.base.model.assertion.IAssertionContext
import com.aemtools.test.base.model.fixture.ITestFixture
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import junit.framework.TestCase

/**
 * @author Dmytro Primshyts
 */
abstract class BasePatternsTest : BaseLightTest() {

  fun testPattern(pattern: ElementPattern<out PsiElement>,
                  text: String,
                  result: Boolean,
                  addCompletionPlaceholder: Boolean = true,
                  fixtureSetup: ITestFixture.(textToAdd: String) -> Unit) = fileCase {
    val textToAdd = preprocessText(text, addCompletionPlaceholder)

    fixtureSetup.invoke(this, textToAdd)
    verify {
      assertEquals(
          assertionMessage(pattern, file, text),
          result,
          pattern.accepts(elementUnderCaret()))
    }
  }

  inline fun <reified T> testCondition(condition: PatternCondition<T>,
                                       text: String,
                                       result: Boolean,
                                       addCompletionPlaceholder: Boolean = true,
                                       crossinline fixtureSetup: ITestFixture.(textToAdd: String) -> Unit,
                                       crossinline elementSelector: IAssertionContext.() -> T) = fileCase {
    val textToAdd = preprocessText(text, addCompletionPlaceholder)

    fixtureSetup.invoke(this, textToAdd)

    verify {
      val element = elementSelector.invoke(this)!!
      TestCase.assertEquals(result,
          condition.accepts(element, null))
    }

  }

  inline fun <reified T> testCondition(condition: PatternCondition<T>,
                                       text: String,
                                       result: Boolean,
                                       addCompletionPlaceholder: Boolean = true,
                                       crossinline fixtureSetup: ITestFixture.(textToAdd: String) -> Unit,
                                       crossinline elementSelector: IAssertionContext.() -> T,
                                       crossinline fixtureTearDown: ITestFixture.() -> Unit) = fileCase {
    testCondition(condition, text, result, addCompletionPlaceholder, fixtureSetup, elementSelector)

    fixtureTearDown.invoke(this)
  }

  fun preprocessText(textToAdd: String, addCompletionPlaceholder: Boolean): String =
      if (addCompletionPlaceholder) {
        textToAdd.addIdeaPlaceholder()
      } else {
        textToAdd
      }

  private fun assertionMessage(pattern: ElementPattern<out PsiElement>,
                               file: PsiFile,
                               text: String): String {
    val builder = StringBuilder()
    with(builder) {
      append("\nPattern:\n$pattern")
      append("\nPSI:\n${DebugUtil.psiToString(file, true)}")
      val htmlFile = file.getHtmlFile()
      if (htmlFile != null) {
        append("PSI Html:\n${DebugUtil.psiToString(htmlFile, true)}")
      }
      append("Text: $text")
    }
    return builder.toString()
  }

  private fun String.addIdeaPlaceholder(): String =
      StringBuilder(this)
          .insert(this.indexOf(CodeInsightTestFixture.CARET_MARKER)
              + CodeInsightTestFixture.CARET_MARKER.length,
              const.IDEA_STRING_CARET_PLACEHOLDER)
          .toString()

}
